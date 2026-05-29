package com.sp.propertyservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sp.propertyservice.dto.PropertyRequestDTO;
import com.sp.propertyservice.dto.PropertyResponseDTO;
import com.sp.propertyservice.dto.PropertyWithOwnerDTO;
import com.sp.propertyservice.exception.ImageUploadException;
import com.sp.propertyservice.grpc.UserGrpcClient;
import com.sp.propertyservice.kafka.PropertyKafkaProducer;
import com.sp.propertyservice.mapper.PropertyMapper;
import com.sp.propertyservice.model.Property;
import com.sp.propertyservice.repository.PropertyRepository;
import com.sp.user.UserResponse;

import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PropertyService {
    private static final Logger log = LoggerFactory.getLogger(PropertyService.class);
    private final PropertyRepository propertyRepository;
    private Cloudinary cloudinary;
    private final UserGrpcClient userGrpcClient;
    private final PropertyKafkaProducer propertyKafkaProducer;

    public PropertyService(PropertyRepository propertyRepository, Cloudinary cloudinary,
                           UserGrpcClient userGrpcClient,
                           PropertyKafkaProducer propertyKafkaProducer) {
        this.propertyRepository = propertyRepository;
        this.cloudinary = cloudinary;
        this.userGrpcClient = userGrpcClient;
        this.propertyKafkaProducer = propertyKafkaProducer;
    }

    public List<String> uploadImage(List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();

        for(MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty: " + file.getOriginalFilename());
            }
            if (!("image/".equals(file.getContentType()) || (file.getContentType() != null && file.getContentType().startsWith("image/")))) {
                throw new IllegalArgumentException("Only image files are allowed: " + file.getOriginalFilename());
            }
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "properties",
                                        "resource_type", "image"

                        )
                );
                imageUrls.add((String) uploadResult.get("secure_url"));

            } catch (IOException e) {
                throw new ImageUploadException(
                        "Failed to upload image: " +file.getOriginalFilename(), e
                );
            }
        }
        return  imageUrls;
    }

    @Transactional
    public PropertyResponseDTO createProperty(PropertyRequestDTO propertyRequestDTO, List<MultipartFile> images,
                                              UUID userId) {

        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = uploadImage(images);
            propertyRequestDTO.setImageUrls(imageUrls);
        }
        Property property =  PropertyMapper.toModel(propertyRequestDTO);
        property.setUserId(userId);

        Property savedProperty = propertyRepository.save(property);

        // Trigger AI Sync
        syncWithAI(savedProperty, "CREATE");

        return PropertyMapper.toDTO(savedProperty);
    }

    public List<PropertyResponseDTO> getProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream().map(PropertyMapper::toDTO).toList();
    }

    public List<PropertyWithOwnerDTO> getPropertyWithOwner() {
        return propertyRepository.findAll()
                .stream()
                .map(property -> {
                    UserResponse owner = userGrpcClient
                            .getUserById(property.getUserId().toString());
                    return new PropertyWithOwnerDTO(property, owner);
                        })
                .toList();
    }

    @Transactional
    public PropertyResponseDTO updateProperty(UUID id, PropertyRequestDTO dto,
                                              List<MultipartFile> newImages,
                                              UUID loggedInUserId) {
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        validateOwnership(existingProperty, loggedInUserId);

        if (newImages != null && !newImages.isEmpty()) {
            List<String> oldUrls = existingProperty.getImageUrls();
            if (oldUrls != null) {
                deleteImagesFromCloudinary(oldUrls);
            }

            List<String> newUrls = uploadImage(newImages);
            existingProperty.setImageUrls(newUrls);
        }

        existingProperty.setName(dto.getName());
        existingProperty.setPrice(Double.parseDouble(dto.getPrice()));
        existingProperty.setDescription(dto.getDescription());
        existingProperty.setAddress(dto.getAddress());
        existingProperty.setType(dto.getType());

        Property updatedProperty = propertyRepository.save(existingProperty);

        //Trigger AI Sync
        syncWithAI(updatedProperty, "UPDATE");

        return PropertyMapper.toDTO(updatedProperty);
    }

    @Transactional
    public void deleteProperty(UUID id, UUID loggedInUserId, boolean isAdmin) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Property not found"));

        // SECURITY: Validate Ownership OR Admin status
        validateAccess(property, loggedInUserId, isAdmin);

        // Delete images from Cloudinary (using your existing helper)
        // It's better to do this while the property object is still in memory
        if (property.getImageUrls() != null && !property.getImageUrls().isEmpty()) {
            deleteImagesFromCloudinary(property.getImageUrls());
        }

        propertyRepository.delete(property);

        //Trigger AI Sync
        syncWithAI(property, "DELETE");

        log.info("Successfully deleted property and synced with AI: {}", id);
    }

    public List<String> getAllPropertyIds() {
        return propertyRepository.findAllIds()
                .stream()
                .map(UUID::toString)
                .toList();
    }

    public PropertyResponseDTO getPropertyById(UUID id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        return PropertyMapper.toDTO(property);
    }

    @Transactional
    public void deletePropertyByUserId(UUID userId) {
        List<Property> properties = propertyRepository.findByUserId(userId);

        if(properties.isEmpty()) {
            log.info("No properties found for userId: {}", userId);
            return;
        }

        for (Property property : properties) {
            if (property.getImageUrls() != null && !property.getImageUrls().isEmpty()) {
                deleteImagesFromCloudinary(property.getImageUrls());
            }

            propertyRepository.delete(property);
        }
        log.info("Deleted {} properties for userId: {}", properties.size(), userId);
    }


    // Private-help-methods

    private void deleteImagesFromCloudinary(List<String> urls) {
        for (String url : urls) {
            try {
                String publicId = extractPublicId(url);
                if (publicId != null) {
                    cloudinary.uploader().destroy(publicId, com.cloudinary.utils.ObjectUtils.emptyMap());
                    log.info("Successfully deleted Cloudinary asset: {}", publicId);
                }

            } catch (IOException e) {
                log.error("Failed to delete image from Cloudinary: {} | Error: {}", url, e.getMessage());
            }
        }
    }

    /**
     * Extracts the Public ID from a Cloudinary URL.
     * Standard format: https://res.cloudinary.com/{cloud_name}/image/upload/{version}/{folder}/{filename}.{ext}
     */
    private String extractPublicId(String url) {
        try {
            // Split by '/' to isolate the folder and filename
            String[] parts = url.split("/");

            // The filename is the last part: "image_name.jpg"
            String filenameWithExtension = parts[parts.length - 1];

            // The folder is usually the second to last part: "properties"
            String folder = parts[parts.length - 2];

            // Remove the file extension (e.g., .jpg, .png)
            String filename = filenameWithExtension.split("\\.")[0];

            // Cloudinary needs the "folder/filename" as the publicId
            return folder + "/" + filename;
        } catch (Exception e) {
            log.error("Could not parse Public ID from URL: {} ", url);
            return null;
        }
    }

    private void validateOwnership(Property property, UUID loggedInUserId) {
        if (!property.getUserId().equals(loggedInUserId)) {
            log.error("Unauthorized update attempt by user {}", loggedInUserId);
            throw new RuntimeException("Access Denied: You do not own this property.");
        }
    }

    private void validateAccess(Property property, UUID loggedInUserId, boolean isAdmin) {
        // Allow if user is Admin OR if user is the Owner
        if (isAdmin || property.getUserId().equals(loggedInUserId)) {
            return; // Access granted
        }

        log.error("Unauthorized access attempt by user {}", loggedInUserId);
        throw new RuntimeException("Access Denied: You do not have permission to delete this.");
    }

    private void syncWithAI(Property property, String action) {
        try {
            UserResponse owner = null;

            // We only need owner details for CREATE and UPDATE
            // For DELETE, the AI just needs the ID to remove it
            if (!"DELETE".equals(action)) {
                owner = userGrpcClient.getUserById(property.getUserId().toString());
            }

            propertyKafkaProducer.sendPropertyEvent(property, owner, action);
            log.info("Successfully synced {} action to AI for property {}", action, property.getId());
        } catch (Exception e) {
            // We log the error but don't throw it, so the main DB transaction still succeeds
            log.error("AI Sync failed for property {} during {}: {}", property.getId(), action, e.getMessage());
        }
    }


}

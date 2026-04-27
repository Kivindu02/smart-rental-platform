package com.sp.propertyservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sp.propertyservice.dto.PropertyRequestDTO;
import com.sp.propertyservice.dto.PropertyResponseDTO;
import com.sp.propertyservice.dto.PropertyWithOwnerDTO;
import com.sp.propertyservice.exception.ImageUploadException;
import com.sp.propertyservice.grpc.UserGrpcClient;
import com.sp.propertyservice.mapper.PropertyMapper;
import com.sp.propertyservice.model.Property;
import com.sp.propertyservice.repository.PropertyRepository;
import com.sp.user.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private Cloudinary cloudinary;
    private final UserGrpcClient userGrpcClient;

    public PropertyService(PropertyRepository propertyRepository, Cloudinary cloudinary,
                           UserGrpcClient userGrpcClient) {
        this.propertyRepository = propertyRepository;
        this.cloudinary = cloudinary;
        this.userGrpcClient = userGrpcClient;
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
        return PropertyMapper.toDTO(propertyRepository.save(property));
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


}

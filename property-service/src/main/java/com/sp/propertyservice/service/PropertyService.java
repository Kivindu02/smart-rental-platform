package com.sp.propertyservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sp.propertyservice.dto.PropertyRequestDTO;
import com.sp.propertyservice.dto.PropertyResponseDTO;
import com.sp.propertyservice.exception.ImageUploadException;
import com.sp.propertyservice.mapper.PropertyMapper;
import com.sp.propertyservice.model.Property;
import com.sp.propertyservice.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private Cloudinary cloudinary;

    public PropertyService(PropertyRepository propertyRepository, Cloudinary cloudinary) {
        this.propertyRepository = propertyRepository;
        this.cloudinary = cloudinary;
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
    public PropertyResponseDTO createProperty(PropertyRequestDTO propertyRequestDTO, List<MultipartFile> images) {

        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = uploadImage(images);
            propertyRequestDTO.setImageUrls(imageUrls);
        }
        Property property =  propertyRepository.save(PropertyMapper.toModel(propertyRequestDTO));
        return PropertyMapper.toDTO(property);
    }

    public List<PropertyResponseDTO> getProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream().map(PropertyMapper::toDTO).toList();
    }


}

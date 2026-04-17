package com.sp.propertyservice.controller;

import com.sp.propertyservice.dto.PropertyRequestDTO;
import com.sp.propertyservice.model.Property;
import com.sp.propertyservice.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Property> createProperty(
            @RequestPart("property")PropertyRequestDTO
            propertyRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ) {
        Property saved = propertyService.createProperty(propertyRequestDTO, images);
        return ResponseEntity.ok(saved);

    }
}

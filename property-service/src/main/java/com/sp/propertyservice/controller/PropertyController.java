package com.sp.propertyservice.controller;

import com.sp.propertyservice.dto.PropertyRequestDTO;
import com.sp.propertyservice.dto.PropertyResponseDTO;
import com.sp.propertyservice.mapper.PropertyMapper;
import com.sp.propertyservice.model.Property;
import com.sp.propertyservice.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<PropertyResponseDTO> createProperty(
            @RequestPart("property")PropertyRequestDTO
            propertyRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ) {
        PropertyResponseDTO saved = propertyService.createProperty(propertyRequestDTO, images);
        return ResponseEntity.ok(saved);

    }

    @GetMapping
    public ResponseEntity<List<PropertyResponseDTO>> getProperties() {
        List<PropertyResponseDTO> properties = propertyService.getProperties();
        return ResponseEntity.ok().body(properties);
    }
}

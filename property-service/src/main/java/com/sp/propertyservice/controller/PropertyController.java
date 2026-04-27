package com.sp.propertyservice.controller;

import com.sp.propertyservice.dto.PropertyRequestDTO;
import com.sp.propertyservice.dto.PropertyResponseDTO;
import com.sp.propertyservice.dto.PropertyWithOwnerDTO;
import com.sp.propertyservice.mapper.PropertyMapper;
import com.sp.propertyservice.model.Property;
import com.sp.propertyservice.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PropertyResponseDTO> createProperty(
            @Valid @RequestPart("property")PropertyRequestDTO
            propertyRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ) {
        //get userId form the Security Context (set by JwtFilter)
        String userId = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        PropertyResponseDTO saved = propertyService.createProperty(propertyRequestDTO, images, UUID.fromString(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

//    @GetMapping
//    public ResponseEntity<List<PropertyResponseDTO>> getProperties() {
//        List<PropertyResponseDTO> properties = propertyService.getProperties();
//        return ResponseEntity.ok().body(properties);
//    }

    @GetMapping
    public ResponseEntity<List<PropertyWithOwnerDTO>> getPropertyWithOwner() {
        return  ResponseEntity.ok(propertyService.getPropertyWithOwner());
    }
}

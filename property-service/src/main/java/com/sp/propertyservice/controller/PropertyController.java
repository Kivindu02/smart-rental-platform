package com.sp.propertyservice.controller;

import com.sp.propertyservice.dto.PropertyRequestDTO;
import com.sp.propertyservice.dto.PropertyResponseDTO;
import com.sp.propertyservice.dto.PropertyWithOwnerDTO;
import com.sp.propertyservice.mapper.PropertyMapper;
import com.sp.propertyservice.model.Property;
import com.sp.propertyservice.service.PropertyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private static final Logger log = LoggerFactory.getLogger(PropertyController.class);
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
        UUID userId = getAuthenticatedUserId();

        PropertyResponseDTO saved = propertyService.createProperty(propertyRequestDTO, images, userId);
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

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PropertyResponseDTO> updateProperty(
            @PathVariable UUID id,
            @Valid @RequestPart("property") PropertyRequestDTO propertyRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        UUID userId = getAuthenticatedUserId();
        PropertyResponseDTO updated = propertyService.updateProperty(id, propertyRequestDTO, images, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        // We need to pass the roles/authorities to the service too
        var auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getAuthenticatedUserId();

        boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        propertyService.deleteProperty(id, userId, isAdmin);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping("/ids")
    public ResponseEntity<List<String>> getAllPropertyIds() {
        // This helps the AI service verify it hasn't missed any Kafka events
        return ResponseEntity.ok(propertyService.getAllPropertyIds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponseDTO> getPropertyById(@PathVariable UUID id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }


    private UUID getAuthenticatedUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getDetails() == null) {
            log.error("Authentication or details (userId) are missing from the security context");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User identity not verified");
        }

        return UUID.fromString(authentication.getDetails().toString());
    }
}

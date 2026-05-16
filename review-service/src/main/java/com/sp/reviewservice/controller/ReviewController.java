package com.sp.reviewservice.controller;

import com.sp.reviewservice.dto.ReviewRequestDTO;
import com.sp.reviewservice.dto.ReviewResponseDTO;
import com.sp.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/property/{propertyId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponseDTO> createReview(
            @PathVariable UUID propertyId,
            @Valid @RequestBody ReviewRequestDTO dto
            ) {

        UUID userId = getAuthenticatedUserId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(dto, userId, propertyId));
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable UUID reviewId,
            @Valid @RequestBody ReviewRequestDTO dto
    ) {
        UUID userId = getAuthenticatedUserId();
        return ResponseEntity.ok(reviewService.updateReview(reviewId, dto, userId));
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
        UUID userId = getAuthenticatedUserId();
        boolean isAdmin = checkIfAdmin();

        reviewService.deleteReview(reviewId, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    private UUID getAuthenticatedUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getDetails() == null) {
            log.error("Authentication or details (userId) are missing from the security context");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User identity not verified");
        }

        try {
            return UUID.fromString(authentication.getDetails().toString());
        } catch (IllegalArgumentException e) {
            log.error("Failed to parse userId from security details: {}", authentication.getDetails());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user identification format");
        }
    }

    private boolean checkIfAdmin() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return false; // Not authenticated or no roles implies not an admin
        }

        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}

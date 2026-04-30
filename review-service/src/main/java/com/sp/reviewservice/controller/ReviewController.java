package com.sp.reviewservice.controller;

import com.sp.reviewservice.dto.ReviewRequestDTO;
import com.sp.reviewservice.dto.ReviewResponseDTO;
import com.sp.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

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

        String userId = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(dto, UUID.fromString(userId), propertyId));
    }
}

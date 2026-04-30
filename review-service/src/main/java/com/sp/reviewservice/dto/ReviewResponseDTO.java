package com.sp.reviewservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewResponseDTO {

    private final UUID id;
    private final UUID propertyId;
    private final Integer rating;
    private final String comment;
    private final LocalDateTime createdAt;
    private final UserDTO reviewer;

    public ReviewResponseDTO(UUID id, UUID propertyId, Integer rating,
                             String comment, LocalDateTime createdAt, UserDTO reviewer) {
        this.id = id;
        this.propertyId = propertyId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.reviewer = reviewer;
    }

    public UUID getId() {

        return id;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserDTO getReviewer() {
        return reviewer;
    }
}

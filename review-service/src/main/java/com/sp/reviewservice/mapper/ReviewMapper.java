package com.sp.reviewservice.mapper;

import com.sp.property.PropertyResponse;
import com.sp.reviewservice.dto.ReviewRequestDTO;
import com.sp.reviewservice.dto.ReviewResponseDTO;
import com.sp.reviewservice.dto.UserDTO;
import com.sp.reviewservice.model.Review;
import com.sp.user.UserResponse;

import java.util.UUID;

public class ReviewMapper {

    public static Review toModel(ReviewRequestDTO dto, UUID userId, UUID propertyId) {
        Review review = new Review();
        review.setPropertyId(propertyId);
        review.setUserId(userId);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        return review;
    }

    public static ReviewResponseDTO toDTO(Review review,
                                          UserResponse user) {
        UserDTO reviewer = new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNo()

        );

        return new ReviewResponseDTO(
                review.getId(),
                review.getPropertyId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                reviewer

        );

    }
}

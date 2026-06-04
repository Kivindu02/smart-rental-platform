package com.sp.reviewservice.service;

import com.sp.property.PropertyResponse;
import com.sp.reviewservice.dto.ReviewRequestDTO;
import com.sp.reviewservice.dto.ReviewResponseDTO;
import com.sp.reviewservice.exception.AlreadyReviewedException;
import com.sp.reviewservice.exception.PropertyNotFoundException;
import com.sp.reviewservice.grpc.PropertyGrpcClient;
import com.sp.reviewservice.grpc.UserGrpcClient;
import com.sp.reviewservice.kafka.ReviewKafkaProducer;
import com.sp.reviewservice.mapper.ReviewMapper;
import com.sp.reviewservice.model.Review;
import com.sp.reviewservice.repository.ReviewRepository;
import com.sp.user.UserResponse;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepository reviewRepository;
    private final PropertyGrpcClient propertyGrpcClient;
    private final UserGrpcClient userGrpcClient;
    private final ReviewKafkaProducer reviewKafkaProducer;

    public ReviewService(ReviewRepository reviewRepository,
                         PropertyGrpcClient propertyGrpcClient,
                         UserGrpcClient userGrpcClient,
                         ReviewKafkaProducer reviewKafkaProducer) {
        this.reviewRepository = reviewRepository;
        this.propertyGrpcClient = propertyGrpcClient;
        this.userGrpcClient = userGrpcClient;
        this.reviewKafkaProducer = reviewKafkaProducer;
    }

    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO dto,
                                          UUID userId,
                                          UUID propertyId) {

        boolean propertyExists = propertyGrpcClient.propertyExists(propertyId.toString());

        if(!propertyExists) {
            throw new PropertyNotFoundException("Property not found");
        }

        if (reviewRepository.existsByPropertyIdAndUserId(propertyId, userId)) {
            throw  new AlreadyReviewedException("You have already reviewed this property");
        }

        Review review = ReviewMapper.toModel(dto, userId, propertyId);
        Review saved = reviewRepository.save(review);

        syncWithAI(saved, "CREATE");

        UserResponse user = userGrpcClient.getUserById(userId.toString());

        return ReviewMapper.toDTO(saved, user);

    }

    @Transactional
    public ReviewResponseDTO updateReview(UUID reviewId, ReviewRequestDTO dto, UUID loggedInUserId) {
        Review existing = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        validateOwnership(existing, loggedInUserId);

        existing.setRating(dto.getRating());
        existing.setComment(dto.getComment());
        Review updated = reviewRepository.save(existing);

        syncWithAI(updated, "UPDATE");

        UserResponse user = userGrpcClient.getUserById(loggedInUserId.toString());
        return  ReviewMapper.toDTO(updated, user);
    }

    @Transactional
    public void deleteReview(UUID reviewId, UUID loggedInUserId, boolean isAdmin) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        validateAccess(review, loggedInUserId, isAdmin);

        reviewRepository.delete(review);

        syncWithAI(review, "DELETE");

    }

    @Transactional
    public void deleteReviewsByPropertyId(UUID propertyId) {
        List<Review> reviews = reviewRepository.findByPropertyId(propertyId);

        if(reviews.isEmpty()) {
            log.info("No reviews found for propertyId: {}", propertyId);
            return;
        }

        for (Review review: reviews) {
            reviewRepository.delete(review);
            syncWithAI(review, "DELETE");
        }

        log.info("Deleted {} reviews for propertyId: {}", reviews.size(), propertyId);
    }

    // private-methods
    private void syncWithAI(Review review, String action) {
        try {
            UserResponse author = null;

            if (!"DELETE".equals(action)) {
                author = userGrpcClient.getUserById(review.getUserId().toString());
            }

            reviewKafkaProducer.sendReviewEvent(review, author, action);
            log.info("AI Sync triggered for Review: {} with action {}", review.getId(), action);

        } catch (Exception e) {
            log.error("AI Sync failed for review {}: {}", review.getId(), e.getMessage());
        }
    }

    private void validateOwnership(Review review, UUID loggedInUserId) {
        if (!review.getUserId().equals(loggedInUserId)) {
            log.error("Unauthorized update attempt by user {}", loggedInUserId);
            throw new RuntimeException("Access Denied: You do not own this Review .");
        }
    }

    private void validateAccess(Review review, UUID loggedInUserId, boolean isAdmin) {
        // Allow if user is Admin OR if user is the Owner
        if (isAdmin || review.getUserId().equals(loggedInUserId)) {
            return; // Access granted
        }

        log.error("Unauthorized access attempt by user {}", loggedInUserId);
        throw new RuntimeException("Access Denied: You do not have permission to delete this.");
    }


}

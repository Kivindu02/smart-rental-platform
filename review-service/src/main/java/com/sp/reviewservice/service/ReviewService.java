package com.sp.reviewservice.service;

import com.sp.property.PropertyResponse;
import com.sp.reviewservice.dto.ReviewRequestDTO;
import com.sp.reviewservice.dto.ReviewResponseDTO;
import com.sp.reviewservice.exception.AlreadyReviewedException;
import com.sp.reviewservice.exception.PropertyNotFoundException;
import com.sp.reviewservice.grpc.PropertyGrpcClient;
import com.sp.reviewservice.grpc.UserGrpcClient;
import com.sp.reviewservice.mapper.ReviewMapper;
import com.sp.reviewservice.model.Review;
import com.sp.reviewservice.repository.ReviewRepository;
import com.sp.user.UserResponse;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyGrpcClient propertyGrpcClient;
    private final UserGrpcClient userGrpcClient;

    public ReviewService(ReviewRepository reviewRepository, PropertyGrpcClient propertyGrpcClient, UserGrpcClient userGrpcClient) {
        this.reviewRepository = reviewRepository;
        this.propertyGrpcClient = propertyGrpcClient;
        this.userGrpcClient = userGrpcClient;
    }

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

        UserResponse user = userGrpcClient.getUserById(userId.toString());

        return ReviewMapper.toDTO(saved, user);

        }
    }

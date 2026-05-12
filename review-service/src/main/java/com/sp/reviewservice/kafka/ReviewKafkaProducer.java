package com.sp.reviewservice.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.reviewservice.dto.ReviewResponseDTO;
import com.sp.reviewservice.mapper.ReviewMapper;
import com.sp.reviewservice.model.Review;
import com.sp.user.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReviewKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(ReviewKafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "review-service";

    public ReviewKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendReviewEvent(Review review, UserResponse auther, String action) {

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", action);

            if ("DELETE".equals(action) || auther == null) {
                payload.put("id", review.getId().toString());
            } else {
                ReviewResponseDTO enrichedDTO = ReviewMapper.toDTO(review, auther);
                payload.put("data", enrichedDTO);
            }

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(TOPIC, review.getId().toString(), message);

            log.info("Review event {} sent for Review ID: {}", action, review.getId());

        } catch (Exception e) {
            log.error("Failed to sync Review with AI: {}", e.getMessage());
        }

    }
}

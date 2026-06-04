package com.sp.reviewservice.kafka;


import com.sp.reviewservice.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PropertyEventKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(PropertyEventKafkaConsumer.class);
    private final ReviewService reviewService;

    public PropertyEventKafkaConsumer(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @KafkaListener(topics = "property-deleted", groupId = "review-service-group")
    @Transactional
    public  void handlePropertyDeleted(String propertyId) {
        log.info("Received PROPERTY_DELETED event for propertyId: {}", propertyId);
        try {
            reviewService.deleteReviewsByPropertyId(UUID.fromString(propertyId));
            log.info("Successfully deleted reviews for propertyId: {}", propertyId);

        } catch (Exception e) {
            log.error("Failed to delete reviews for propertyId {}", propertyId, e);
        }
    }
}

package com.sp.propertyservice.kafka;

import com.sp.propertyservice.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserEventKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserEventKafkaConsumer.class);
    private final PropertyService propertyService;

    public UserEventKafkaConsumer(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @KafkaListener(topics = "user-deleted", groupId = "property-service-group")
    public void handleUserDeleted(String userId) {
        log.info("Received USER_DELETED event for userId: {}", userId);
        try {
            propertyService.deletePropertyByUserId(UUID.fromString(userId));
        } catch (Exception e) {
            log.error("Failed to handle USER_DELETED for userId {}: {}", userId, e.getMessage());
        }
    }
}

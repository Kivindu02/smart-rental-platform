package com.sp.propertyservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.propertyservice.dto.PropertyWithOwnerDTO;
import com.sp.propertyservice.model.Property;
import com.sp.user.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class PropertyKafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(PropertyKafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "property-events";
    private static final String PROPERTY_DELETED_TOPIC = "property-deleted";

    public PropertyKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendPropertyEvent(Property property, UserResponse owner, String action) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", action);

            if ("DELETE".equals(action) || owner == null) {
                // For delete, the AI only needs the ID to remove it from the vector store
                payload.put("id", property.getId().toString());
            } else {
                // For CREATE/UPDATE, use your enriched DTO
                PropertyWithOwnerDTO enrichedData = new PropertyWithOwnerDTO(property, owner);
                payload.put("data", enrichedData);
            }

            // Convert to JSON string
            String message = objectMapper.writeValueAsString(payload);

            // Send to Kafka (using property ID as the message key)
            kafkaTemplate.send(TOPIC, property.getId().toString(), message);

            log.info("Property event {} sent for ID: {}", action, property.getId());
        }catch (Exception e) {
            log.error("AI Sync Failed for Property {}: {}", property.getId(), e.getMessage());
        }
    }

    public void sendPropertyDeletedEvent(UUID propertyId) {
        try {
            kafkaTemplate.send(PROPERTY_DELETED_TOPIC, propertyId.toString());
            log.info("PROPERTY_DELETED event sent for propertyId: {}", propertyId);
        } catch (Exception e) {
            log.error("Failed to send PROPERTY_DELETED event for {}: {}", propertyId, e.getMessage());
        }
    }
}

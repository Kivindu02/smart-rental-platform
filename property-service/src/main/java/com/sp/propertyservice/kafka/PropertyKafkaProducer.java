package com.sp.propertyservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.propertyservice.model.Property;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class PropertyKafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(PropertyKafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "property-events";

    public PropertyKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendPropertyEvent(Property property, String action) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", action);
            payload.put("id", property.getId().toString());
            payload.put("name", property.getName());
            payload.put("address", property.getAddress());
            payload.put("type", property.getType());
            payload.put("price", property.getPrice());
            payload.put("description", property.getDescription());
            payload.put("userId", property.getUserId() != null ? property.getUserId().toString() : "");
            payload.put("imageUrls", property.getImageUrls());

            // Convert to JSON string
            String message = objectMapper.writeValueAsString(payload);

            // Send to Kafka (using property ID as the message key)
            kafkaTemplate.send(TOPIC, property.getId().toString(), message);
            log.info("Property event {} sent for ID: {}", action, property.getId());
        }catch (Exception e) {
            log.error("AI Sync Failed for Property {}: {}", property.getId(), e.getMessage());
        }
    }
}

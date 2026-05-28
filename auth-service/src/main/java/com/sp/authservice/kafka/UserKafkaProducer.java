package com.sp.authservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserKafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(UserKafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String USER_DELETED_TOPIC = "user-deleted";

    public UserKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserDeleteEvent (UUID userId) {
        kafkaTemplate.send(USER_DELETED_TOPIC, userId.toString());
        log.info("Published USER_DELETED event for userId: {}", userId);
    }
}

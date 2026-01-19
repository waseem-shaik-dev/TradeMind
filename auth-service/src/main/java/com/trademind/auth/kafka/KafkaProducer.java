package com.trademind.auth.kafka;

import com.trademind.auth.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserCreated(AuthUser user) {
        kafkaTemplate.send("USER_CREATED_TOPIC", user.getId().toString());
    }
}

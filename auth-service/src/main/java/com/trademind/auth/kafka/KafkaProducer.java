package com.trademind.auth.kafka;


import com.trademind.auth.entity.AuthUser;
import com.trademind.events.UserAccountStatusEvent;
import com.trademind.events.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserCreated(AuthUser user) {

        UserCreatedEvent event = new UserCreatedEvent(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );

        kafkaTemplate.send("USER_CREATED_TOPIC", event);
    }


    public void publishUserSoftDeleted(Long userId) {
        kafkaTemplate.send(
                "USER_ACCOUNT_STATUS_TOPIC",
                new UserAccountStatusEvent(userId, "SOFT_DELETE")
        );
    }

    public void publishUserRestored(Long userId) {
        kafkaTemplate.send(
                "USER_ACCOUNT_STATUS_TOPIC",
                new UserAccountStatusEvent(userId, "RESTORE")
        );
    }
}

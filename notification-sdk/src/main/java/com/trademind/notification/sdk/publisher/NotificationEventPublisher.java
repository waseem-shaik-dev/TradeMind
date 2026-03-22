package com.trademind.notification.sdk.publisher;

import com.trademind.events.notification.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private static final String TOPIC = "notification-topic";

    public void publish(NotificationEvent event) {

        try {
            kafkaTemplate.send(
                    TOPIC,
                    event.getType().name(),
                    event
            );

            log.info("Notification event published: {}", event.getType());

        } catch (Exception ex) {
            log.error("Failed to publish notification event", ex);
            // ❗ Do NOT break business logic
        }
    }
}
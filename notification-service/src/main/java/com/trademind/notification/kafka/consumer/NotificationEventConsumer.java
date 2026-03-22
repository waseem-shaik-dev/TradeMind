package com.trademind.notification.kafka.consumer;

import com.trademind.events.notification.NotificationEvent;
import com.trademind.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${kafka.topics.notification}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(NotificationEvent event) {

        try {
            log.info("Received notification event: {}", event.getType());

            notificationService.processEvent(event);

        } catch (Exception ex) {

            log.error("Failed to process notification event: {}", event, ex);

            // 🔥 Important:
            // Throw exception to trigger retry / DLQ (if configured)
            throw ex;
        }
    }
}
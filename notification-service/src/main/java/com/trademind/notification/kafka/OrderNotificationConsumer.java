package com.trademind.notification.kafka;

import com.trademind.notification.enums.NotificationType;
import com.trademind.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderNotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "ORDER_PLACED_TOPIC",
            groupId = "notification-service"
    )
    public void onOrderPlaced(Map<String, Object> event) {

        notificationService.sendNotification(
                (String) event.get("email"),
                NotificationType.ORDER_PLACED,
                "Your order has been placed successfully."
        );
    }
}

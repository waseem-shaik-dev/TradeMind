package com.trademind.notification.kafka;

import com.trademind.notification.enums.NotificationType;
import com.trademind.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillingNotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "BILL_CREATED_TOPIC",
            groupId = "notification-service"
    )
    public void onBillCreated(Map<String, Object> event) {

        notificationService.sendNotification(
                (String) event.get("email"),
                NotificationType.BILL_CREATED,
                "Your bill has been generated. Bill ID: " + event.get("billId")
        );
    }
}

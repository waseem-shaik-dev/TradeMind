package com.trademind.notification.sdk.util;

import com.trademind.events.notification.NotificationEvent;
import com.trademind.events.notification.enums.NotificationType;

import java.util.Map;
import java.util.UUID;

public class NotificationEventBuilder {

    public static NotificationEvent build(
            NotificationType type,
            String recipient,
            String template,
            String subject,
            Map<String, Object> data
    ) {

        return new NotificationEvent(
                UUID.randomUUID(),
                type,
                recipient,
                template,
                data,
                subject,
                Map.of()
        );
    }
}
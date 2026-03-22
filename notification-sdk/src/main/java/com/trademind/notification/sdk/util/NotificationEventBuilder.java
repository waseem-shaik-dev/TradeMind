package com.trademind.notification.sdk.util;

import com.trademind.events.notification.NotificationEvent;
import com.trademind.events.notification.enums.NotificationType;

import java.util.Map;

public class NotificationEventBuilder {

    public static NotificationEvent build(
            NotificationType type,
            String recipient,
            String template,
            String subject,
            Map<String, Object> data
    ) {

        return NotificationEvent.builder()
                .type(type)
                .recipient(recipient)
                .template(template)
                .subject(subject)
                .data(data)
                .build();
    }
}
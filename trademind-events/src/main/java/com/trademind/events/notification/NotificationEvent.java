package com.trademind.events.notification;

import com.trademind.events.notification.enums.NotificationType;


import java.util.Map;
import java.util.UUID;

public record NotificationEvent (
        UUID eventId,
        // 🔹 Event type
         NotificationType type,

        // 🔹 Recipient email
         String recipient,

        // 🔹 Template override (optional)
         String template,

        // 🔹 Dynamic data
         Map<String, Object> data,

        // 🔹 Subject override
         String subject,

        // 🔹 Metadata (tracking/debugging)
         Map<String, Object> metadata
)
{}
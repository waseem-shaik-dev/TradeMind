package com.trademind.events.notification;

import com.trademind.events.notification.enums.NotificationType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    // 🔹 Event type
    private NotificationType type;

    // 🔹 Recipient email
    private String recipient;

    // 🔹 Template override (optional)
    private String template;

    // 🔹 Dynamic data
    private Map<String, Object> data;

    // 🔹 Subject override
    private String subject;

    // 🔹 Metadata (tracking/debugging)
    private Map<String, Object> metadata;
}
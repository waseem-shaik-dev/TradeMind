package com.trademind.notification.dto;

import com.trademind.events.notification.enums.NotificationType;
import com.trademind.notification.enums.NotificationStatus;

import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private UUID id;

    private String recipient;

    private String subject;

    private NotificationType type;

    private NotificationStatus status;

    private int retryCount;

    private String errorMessage;

    private Map<String, Object> metadata;

    private Instant createdAt;
    private Instant sentAt;
}
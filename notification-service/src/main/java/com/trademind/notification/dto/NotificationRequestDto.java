package com.trademind.notification.dto;


import com.trademind.events.notification.enums.NotificationType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {

    private String recipient;

    private String subject;

    private String body;

    private NotificationType type;

    private Map<String, Object> metadata;
}
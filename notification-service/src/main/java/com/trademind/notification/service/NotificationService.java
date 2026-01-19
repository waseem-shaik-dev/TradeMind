package com.trademind.notification.service;

import com.trademind.notification.enums.NotificationType;

public interface NotificationService {
    void sendNotification(
            String email,
            NotificationType type,
            String message
    );
}

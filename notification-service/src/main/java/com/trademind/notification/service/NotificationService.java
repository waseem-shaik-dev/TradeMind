package com.trademind.notification.service;

import com.trademind.events.notification.NotificationEvent;

public interface NotificationService {

    /**
     * Process incoming Kafka event
     */
    void processEvent(NotificationEvent event);

    /**
     * Retry failed notifications
     */
    void retryFailedNotifications();
}
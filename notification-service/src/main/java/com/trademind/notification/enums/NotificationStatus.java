package com.trademind.notification.enums;

public enum NotificationStatus {

    PENDING,     // created but not sent
    SENT,        // successfully sent
    FAILED,      // failed after retries
    RETRYING     // retry in progress
}
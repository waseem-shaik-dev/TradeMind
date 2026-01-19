package com.trademind.notification.dto;

public record NotificationEvent(
        String email,
        String type,
        String message
) {}

package com.trademind.user.dto;

public record UserCreatedEvent(
        Long userId,
        String username,
        String email
) {}


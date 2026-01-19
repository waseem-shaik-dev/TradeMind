package com.trademind.auth.dto;

public record UserCreatedEvent(
        Long userId,
        String username,
        String email
) {}


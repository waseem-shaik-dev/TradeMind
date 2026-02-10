package com.trademind.events;

public record UserCreatedEvent(
        Long userId,
        String username,
        String email,
        String role
) {}


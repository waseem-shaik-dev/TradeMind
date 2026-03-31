package com.trademind.auth.dto;

public record AuthResponse(
        Long userId,
        String userRole,
        String accessToken,
        String userEmail
) {}

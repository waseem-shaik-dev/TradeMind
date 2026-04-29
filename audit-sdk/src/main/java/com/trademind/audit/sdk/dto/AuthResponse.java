package com.trademind.audit.sdk.dto;

public record AuthResponse(
        Long userId,
        String userRole,
        String accessToken,
        String userEmail
) {}

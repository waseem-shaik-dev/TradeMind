package com.trademind.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}

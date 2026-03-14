package com.trademind.auth.dto;

public record RefreshResponse(
        Long userId,
        String role,
        String accessToken
) {}


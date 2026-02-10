package com.trademind.user.dto;

public record UserSummaryResponse(
        Long userId,
        String fullName,
        String avatarUrl,
        String role
) {}

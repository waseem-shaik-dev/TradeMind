package com.trademind.user.dto;

public record UserProfileDto(
        String fullName,
        String phone,
        String email,
        String avatarUrl
) {}

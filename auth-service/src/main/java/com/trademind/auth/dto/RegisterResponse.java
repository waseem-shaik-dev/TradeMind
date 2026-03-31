package com.trademind.auth.dto;

import com.trademind.auth.enums.Role;

public record RegisterResponse(
        Long userId,
        String role,
        String userEmail
) {}

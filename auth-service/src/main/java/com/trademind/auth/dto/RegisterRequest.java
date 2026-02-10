package com.trademind.auth.dto;

import com.trademind.auth.enums.Role;

public record RegisterRequest(
        String username,
        String email,
        String password,
        Role role
) {}

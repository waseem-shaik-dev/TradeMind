package com.trademind.auth.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}

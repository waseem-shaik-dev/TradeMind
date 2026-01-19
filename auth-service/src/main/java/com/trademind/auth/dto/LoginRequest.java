package com.trademind.auth.dto;

public record LoginRequest(
        String username,
        String password
) {}

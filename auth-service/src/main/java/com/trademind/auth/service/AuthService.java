package com.trademind.auth.service;

import com.trademind.auth.dto.AuthResponse;
import com.trademind.auth.dto.LoginRequest;
import com.trademind.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void register(RegisterRequest request);
    AuthResponse refreshToken(String refreshToken);
}

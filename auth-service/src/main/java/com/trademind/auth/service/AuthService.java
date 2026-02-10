package com.trademind.auth.service;

import com.trademind.auth.dto.AuthResponse;
import com.trademind.auth.dto.LoginRequest;
import com.trademind.auth.dto.RegisterRequest;
import com.trademind.auth.dto.RegisterResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request, String requesterRole);
    void softDeleteUser(Long userId, String requesterRole);
    void restoreUser(Long userId, String requesterRole);
}

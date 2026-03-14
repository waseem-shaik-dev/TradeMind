package com.trademind.auth.service;

import com.trademind.auth.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse login( LoginRequest request,
                        HttpServletRequest httpRequest,
                        HttpServletResponse response);
    RegisterResponse register(RegisterRequest request, String requesterRole);
    void softDeleteUser(Long userId, String requesterRole);
    void restoreUser(Long userId, String requesterRole);
    RefreshResponse refresh(
            HttpServletRequest request,
            HttpServletResponse response
    );
    void logout(HttpServletRequest request,
                HttpServletResponse response);
}

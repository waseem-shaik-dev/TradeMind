package com.trademind.auth.controller;

import com.trademind.auth.dto.*;
import com.trademind.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req,
                                                     @RequestHeader(value = "X-USER-ROLE",required = false) String requesterRole) {
        return ResponseEntity.ok(
                authService.register(req, requesterRole)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req,
                                              HttpServletRequest httpRequest,
                                              HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(req,httpRequest,response));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable Long userId,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        authService.softDeleteUser(userId, role);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}/restore")
    public ResponseEntity<Void> restoreUser(
            @PathVariable Long userId,
            @RequestHeader("X-USER-ROLE") String role
    ) {
        authService.restoreUser(userId, role);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(
                authService.refresh(request, response)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }


}

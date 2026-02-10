package com.trademind.auth.controller;

import com.trademind.auth.dto.AuthResponse;
import com.trademind.auth.dto.LoginRequest;
import com.trademind.auth.dto.RegisterRequest;
import com.trademind.auth.dto.RegisterResponse;
import com.trademind.auth.service.AuthService;
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
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
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



}

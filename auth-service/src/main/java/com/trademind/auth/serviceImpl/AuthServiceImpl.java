package com.trademind.auth.serviceImpl;

import com.trademind.auth.dto.*;
import com.trademind.auth.entity.*;
import com.trademind.auth.enums.Role;
import com.trademind.auth.kafka.KafkaProducer;
import com.trademind.auth.repository.*;
import com.trademind.auth.security.JwtService;
import com.trademind.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final KafkaProducer producer;

    @Override
    public void register(RegisterRequest req) {

        AuthUser user = AuthUser.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .role(Role.USER) // DEFAULT ROLE
                .enabled(true)
                .locked(false)
                .build();

        userRepo.save(user);
        producer.publishUserCreated(user);
    }

    @Override
    public AuthResponse login(LoginRequest req) {

        AuthUser user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepo.save(user);

        String jwt = jwtService.generateToken(user);
        String refresh = UUID.randomUUID().toString();

        refreshRepo.save(
                new RefreshToken(
                        null,
                        refresh,
                        LocalDateTime.now().plusDays(7),
                        false,
                        user
                )
        );

        return new AuthResponse(jwt, refresh);
    }

    @Override
    public AuthResponse refreshToken(String token) {

        RefreshToken rt = refreshRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (rt.isRevoked() || rt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        return new AuthResponse(
                jwtService.generateToken(rt.getUser()),
                token
        );
    }
}

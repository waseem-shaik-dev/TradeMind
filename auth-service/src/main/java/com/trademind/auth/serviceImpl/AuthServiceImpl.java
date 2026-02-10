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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final KafkaProducer producer;

    @Override
    public RegisterResponse register(RegisterRequest req, String requesterRole) {

        // 🔐 SECURITY CHECK
        if (req.role() != Role.CUSTOMER) {
            if (requesterRole == null || !requesterRole.equals("ADMIN")) {
                throw new RuntimeException(
                        "Only ADMIN can create users with role " + req.role()
                );
            }
        }

        AuthUser user = AuthUser.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .role(req.role())
                .enabled(true)
                .locked(false)
                .build();

        userRepo.save(user);

        producer.publishUserCreated(user);

        return new RegisterResponse(
                user.getId(),
                user.getRole().name()
        );
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

        return new AuthResponse(
                user.getId(),
                user.getRole().name(),
                jwt
        );
    }

    @Override
    public void softDeleteUser(Long userId, String requesterRole) {

        validateAdmin(requesterRole);

        AuthUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled() && user.isLocked()) return;

        user.setEnabled(false);
        user.setLocked(true);
        userRepo.save(user);

        producer.publishUserSoftDeleted(userId);
    }

    @Override
    public void restoreUser(Long userId, String requesterRole) {

        validateAdmin(requesterRole);

        AuthUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled() && !user.isLocked()) return;

        user.setEnabled(true);
        user.setLocked(false);
        userRepo.save(user);

        producer.publishUserRestored(userId);
    }

    private void validateAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Only ADMIN can perform this operation");
        }
    }


}

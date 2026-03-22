package com.trademind.auth.serviceImpl;

import com.trademind.audit.sdk.annotation.AuditLoggable;
import com.trademind.auth.dto.*;
import com.trademind.auth.entity.*;
import com.trademind.auth.enums.Role;
import com.trademind.auth.kafka.KafkaProducer;
import com.trademind.auth.repository.*;
import com.trademind.auth.security.JwtService;
import com.trademind.auth.service.AuthService;
import com.trademind.auth.service.RefreshTokenService;
import com.trademind.events.audit.enums.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final KafkaProducer producer;
    private final RefreshTokenService refreshService;

    private static final String REFRESH_COOKIE = "trademind_refresh";

    @AuditLoggable(
            action = AuditAction.USER_REGISTERED,
            entityType = EntityType.USER,
            entityIdExpression = "#result.userId",
            captureAfter = true
    )
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


    @AuditLoggable(
            action = AuditAction.USER_LOGIN,
            entityType = EntityType.USER,
            entityIdExpression = "#result.userId"
    )
    @Override
    public AuthResponse login(LoginRequest req,
                              HttpServletRequest httpRequest,
                              HttpServletResponse response) {

        AuthUser user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepo.save(user);

        String accessToken = jwtService.generateToken(user);

        String refreshToken =
                refreshService.createRefreshToken(
                        user,
                        httpRequest.getHeader("User-Agent"),
                        httpRequest.getRemoteAddr()
                );

        addRefreshCookie(response, refreshToken);

        return new AuthResponse(
                user.getId(),
                user.getRole().name(),
                accessToken
        );
    }

    @AuditLoggable(
            action = AuditAction.USER_DELETED,
            entityType = EntityType.USER,
            entityIdExpression = "#userId",
            captureBefore = true,
            captureAfter = true
    )
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


    @AuditLoggable(
            action = AuditAction.USER_UPDATED,
            entityType = EntityType.USER,
            entityIdExpression = "#userId",
            captureBefore = true,
            captureAfter = true
    )
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



    @Override
    public RefreshResponse refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String rawToken = extractRefreshToken(request);

        RefreshToken oldToken =
                refreshService.validateToken(rawToken);

        AuthUser user = oldToken.getUser();

        // 🔁 ROTATION
        oldToken.setRevoked(true);
        oldToken.setReused(true);

        String newRefresh =
                refreshService.createRefreshToken(
                        user,
                        request.getHeader("User-Agent"),
                        request.getRemoteAddr()
                );

        addRefreshCookie(response, newRefresh);

        String newAccessToken =
                jwtService.generateToken(user);

        return new RefreshResponse( user.getId(),
                user.getRole().name(),
                newAccessToken);
    }


    @AuditLoggable(
            action = AuditAction.USER_LOGOUT,
            entityType = EntityType.USER
    )
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) {

        String rawToken = extractRefreshToken(request);

        RefreshToken token =
                refreshService.validateToken(rawToken);

        refreshService.revokeToken(token);

        clearCookie(response);
    }

    private void addRefreshCookie(
            HttpServletResponse response,
            String token
    ) {

        ResponseCookie cookie =
                ResponseCookie.from(REFRESH_COOKIE, token)
                        .httpOnly(true)
                        .secure(false) //  HTTP /HTTPS
                        .path("/")
                        .maxAge(Duration.ofDays(7))
                        .sameSite("Strict")
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }


    private String extractRefreshToken(
            HttpServletRequest request
    ) {

        if (request.getCookies() == null)
            throw new RuntimeException("No refresh token");

        for (Cookie cookie : request.getCookies()) {
            if (REFRESH_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new RuntimeException("Refresh token missing");
    }

    private void clearCookie(HttpServletResponse response) {

        ResponseCookie cookie =
                ResponseCookie.from(REFRESH_COOKIE, "")
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(0)
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }

}

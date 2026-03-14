package com.trademind.auth.serviceImpl;

import com.trademind.auth.entity.AuthUser;
import com.trademind.auth.entity.RefreshToken;
import com.trademind.auth.repository.RefreshTokenRepository;
import com.trademind.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl
        implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    private static final long REFRESH_DAYS = 7;

    private String hash(String rawToken) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");
            byte[] encoded =
                    digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encoded);
        } catch (Exception e) {
            throw new RuntimeException("Token hashing failed");
        }
    }

    @Override
    public String createRefreshToken(
            AuthUser user,
            String deviceInfo,
            String ipAddress
    ) {

        String rawToken = UUID.randomUUID().toString();
        String hash = hash(rawToken);

        RefreshToken token = RefreshToken.builder()
                .tokenHash(hash)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(REFRESH_DAYS))
                .revoked(false)
                .reused(false)
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .build();

        repository.save(token);

        return rawToken; // ONLY return raw token
    }

    @Override
    public RefreshToken validateToken(String rawToken) {

        String hash = hash(rawToken);

        RefreshToken token =
                repository.findByTokenHash(hash)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid refresh token"));

        if (token.isRevoked())
            throw new RuntimeException("Refresh token revoked");

        if (token.isReused())
            throw new RuntimeException("Token reuse detected");

        if (token.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Refresh token expired");

        return token;
    }

    @Override
    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        repository.save(token);
    }

    @Override
    public void revokeAllUserTokens(AuthUser user) {
        repository.revokeAllByUser(user);
    }
}


package com.trademind.auth.service;

import com.trademind.auth.entity.AuthUser;
import com.trademind.auth.entity.RefreshToken;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    String createRefreshToken(
            AuthUser user,
            String deviceInfo,
            String ipAddress
    );

    RefreshToken validateToken(String rawToken);

    void revokeToken(RefreshToken token);

    void revokeAllUserTokens(AuthUser user);
}

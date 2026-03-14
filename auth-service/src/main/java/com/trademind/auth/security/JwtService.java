package com.trademind.auth.security;

import com.trademind.auth.entity.AuthUser;
import com.trademind.auth.exception.AccessTokenExpiredException;
import com.trademind.auth.exception.InvalidJwtSignatureException;
import com.trademind.auth.exception.JwtValidationException;
import com.trademind.auth.exception.MalformedJwtExceptionCustom;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET =
            "THIS_IS_A_VERY_LONG_AND_SECURE_SECRET_KEY_FOR_JWT_SIGNING_256_BITS";

    private static final long JWT_EXPIRY = 55 * 60 * 1000; // 15 min

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(AuthUser user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException("Access token expired");

        } catch (UnsupportedJwtException e) {
            throw new JwtValidationException("Unsupported JWT token");

        } catch (MalformedJwtException e) {
            throw new MalformedJwtExceptionCustom("Malformed JWT token");

        } catch (SignatureException e) {
            throw new InvalidJwtSignatureException("Invalid JWT signature");

        } catch (IllegalArgumentException e) {
            throw new JwtValidationException("JWT token compact of handler are invalid");
        }
    }

}

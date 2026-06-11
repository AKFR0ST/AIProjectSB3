package com.sb3.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import java.util.UUID;


@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username, String role) {
        return generateToken(username, role, expiration);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, null, refreshExpiration);
    }

    private String generateToken(String username, String role, Long expirationSeconds) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expirationSeconds * 1000);

        var builder = Jwts.builder()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .id(UUID.randomUUID().toString())
                .signWith(getSigningKey());

        if (role != null) {
            builder.claim("role", role);
        }

        return builder.compact();
    }

    // Проверка валидности токена (без проверки username)
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка валидности токена с проверкой username
    public boolean isTokenValid(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            Claims claims = parseToken(token).getPayload();
            return "refresh".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    public String extractRole(String token) {
        return parseToken(token).getPayload().get("role", String.class);
    }

    public Date extractExpiration(String token) {
        return parseToken(token).getPayload().getExpiration();
    }

    public Date extractIssuedAt(String token) {
        return parseToken(token).getPayload().getIssuedAt();
    }

    public long getExpiresIn(String token) {
        Date expiration = extractExpiration(token);
        Date now = new Date();
        return (expiration.getTime() - now.getTime()) / 1000;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
    }
}

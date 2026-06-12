package com.sb3.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String email;
    private String fullName;
    private String specialization;
    private String role;
    private Long expiresIn;           // ← срок жизни токена в секундах
    private LocalDateTime issuedAt;   // ← когда выдан
    private LocalDateTime expiresAt;  // ← когда истекает
}

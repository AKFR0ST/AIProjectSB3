package com.sb3.service;

import com.sb3.dto.auth.AuthResponse;
import com.sb3.dto.auth.ChangePasswordRequest;
import com.sb3.dto.auth.LoginRequest;
import com.sb3.entity.teacher.Teacher;
import com.sb3.repository.TeacherRepository;
import com.sb3.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import com.sb3.dto.auth.RefreshTokenRequest;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Teacher teacher = (Teacher) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(teacher.getEmail(), teacher.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(teacher.getEmail());

        Date issuedAt = jwtService.extractIssuedAt(accessToken);
        Date expiresAt = jwtService.extractExpiration(accessToken);

        log.info("User logged in successfully: {} with role: {}", teacher.getEmail(), teacher.getRole());

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .email(teacher.getEmail())
                .fullName(teacher.getFirstName() + " " + teacher.getLastName())
                .specialization(teacher.getSpecialization())
                .role(teacher.getRole().name())
                .expiresIn(jwtService.getExpiresIn(accessToken))
                .issuedAt(LocalDateTime.ofInstant(issuedAt.toInstant(), ZoneId.systemDefault()))
                .expiresAt(LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault()))
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refresh token request");

        String refreshToken = request.getRefreshToken();

        // Валидация refresh token
        if (!jwtService.isTokenValid(refreshToken) || !jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = jwtService.extractUsername(refreshToken);

        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (!teacher.isEnabled()) {
            throw new RuntimeException("Teacher account is disabled");
        }

        // Генерируем новые токены
        String newAccessToken = jwtService.generateAccessToken(teacher.getEmail(), teacher.getRole().name());
        String newRefreshToken = jwtService.generateRefreshToken(teacher.getEmail());

        Date issuedAt = jwtService.extractIssuedAt(newAccessToken);
        Date expiresAt = jwtService.extractExpiration(newAccessToken);

        log.info("Token refreshed successfully for: {}", teacher.getEmail());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .email(teacher.getEmail())
                .fullName(teacher.getFirstName() + " " + teacher.getLastName())
                .specialization(teacher.getSpecialization())
                .role(teacher.getRole().name())
                .expiresIn(jwtService.getExpiresIn(newAccessToken))
                .issuedAt(LocalDateTime.ofInstant(issuedAt.toInstant(), ZoneId.systemDefault()))
                .expiresAt(LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault()))
                .build();
    }

    @Transactional
    public void changePassword(Long teacherId, ChangePasswordRequest request) {
        log.info("Changing password for teacher id: {}", teacherId);

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), teacher.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        teacher.setPassword(passwordEncoder.encode(request.getNewPassword()));
        teacher.setPasswordUpdatedAt(LocalDateTime.now());
        teacherRepository.save(teacher);

        log.info("Password changed successfully for teacher: {}", teacher.getEmail());
    }

    // Опционально: логаут
    public void logout(String token) {
        log.info("Logout request");
        // Пока просто логируем
    }
}
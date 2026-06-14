package com.sb3.config;

import com.sb3.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private static final String[] SWAGGER_UI = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };

    private static final String TEACHERS = "/api/teachers";
    private static final String TEACHER_ID = "/api/teachers/{id}";
    private static final String STUDENTS = "/api/students";
    private static final String STUDENT_ID = "/api/students/{id}";
    private static final String TASKS = "/api/tasks";
    private static final String TASK_ID = "/api/tasks/{id}";

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Публичные эндпоинты
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/refresh").permitAll()
                        .requestMatchers(SWAGGER_UI).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/teachers").permitAll()

                        // AI_AGENT эндпоинты
                        .requestMatchers("/api/llm/**").hasAnyRole("AI_AGENT", "ADMIN")
                        .requestMatchers("/api/entities/**").hasAnyRole("AI_AGENT", "TEACHER", "ADMIN")

                        // ADMIN эндпоинты (управление преподавателями)
                        .requestMatchers(HttpMethod.POST, TEACHERS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, TEACHER_ID).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, TEACHER_ID + "/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, TEACHER_ID + "/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, TEACHER_ID + "/password-updated").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, TEACHER_ID).hasRole("ADMIN")

                        // Просмотр преподавателей доступен ADMIN и TEACHER
                        .requestMatchers(HttpMethod.GET, TEACHERS).hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, TEACHER_ID).hasAnyRole("ADMIN", "TEACHER")

                        // Ученики (CRUD) — доступно TEACHER и ADMIN
                        .requestMatchers(HttpMethod.GET, STUDENTS).hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, STUDENT_ID).hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, STUDENTS).hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, STUDENT_ID).hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, STUDENT_ID + "/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, STUDENT_ID).hasAnyRole("TEACHER", "ADMIN")

                        // Задачи
                        .requestMatchers(HttpMethod.POST, TASKS).hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, TASKS).hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, TASK_ID).hasAnyRole("TEACHER", "ADMIN")

                        // Аутентифицированные эндпоинты
                        .requestMatchers("/api/auth/logout").authenticated()
                        .requestMatchers("/api/auth/change-password").authenticated()
                        .requestMatchers("/api/auth/me").authenticated()

                        // Всё остальное
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
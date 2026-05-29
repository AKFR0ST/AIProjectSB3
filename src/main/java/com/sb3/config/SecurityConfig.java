package com.sb3.config;

import com.sb3.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.sb3.constant.SecurityConstants.ROLE_ADMIN;
import static com.sb3.constant.SecurityConstants.ROLE_USER;



import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.sb3.constant.ApiPaths.*;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(SWAGGER_UI, V_3_API_DOCS, SWAGGER_UI_HTML).permitAll()
                        .requestMatchers("/api/entities/**", "/api/llm/**", "/api/students/*")
                        .hasAnyRole("USER", "ADMIN", "SERVICE")
                        .requestMatchers(HttpMethod.POST, TASKS).hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user")
                .roles(ROLE_USER)
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles(ROLE_ADMIN)
                .build();

        UserDetails agent = User.builder()
                .username("agent")
                .password("{noop}agent-secret-password")
                .roles("SERVICE")
                .build();

        return new InMemoryUserDetailsManager(user, admin, agent);
    }
}
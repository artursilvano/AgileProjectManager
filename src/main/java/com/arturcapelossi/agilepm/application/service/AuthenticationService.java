package com.arturcapelossi.agilepm.application.service;

import com.arturcapelossi.agilepm.api.dto.request.LoginRequest;
import com.arturcapelossi.agilepm.api.dto.request.RegisterRequest;
import com.arturcapelossi.agilepm.api.dto.response.AuthResponse;
import com.arturcapelossi.agilepm.domain.model.enums.Role;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.UserEntity;
import com.arturcapelossi.agilepm.infrastructure.persistence.repository.SpringDataUserRepository;
import com.arturcapelossi.agilepm.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SpringDataUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.MEMBER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}


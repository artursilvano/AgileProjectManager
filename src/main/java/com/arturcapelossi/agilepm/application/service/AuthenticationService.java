package com.arturcapelossi.agilepm.application.service;

import com.arturcapelossi.agilepm.api.dto.request.LoginRequest;
import com.arturcapelossi.agilepm.api.dto.request.RegisterRequest;
import com.arturcapelossi.agilepm.api.dto.response.AuthResponse;
import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.model.enums.Role;
import com.arturcapelossi.agilepm.domain.repository.UserRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.UserMapper;
import com.arturcapelossi.agilepm.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthResponse register(RegisterRequest request) {
        var user = User.create(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.MEMBER
        );

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(userMapper.toEntity(user));
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
        var jwtToken = jwtService.generateToken(userMapper.toEntity(user));
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}

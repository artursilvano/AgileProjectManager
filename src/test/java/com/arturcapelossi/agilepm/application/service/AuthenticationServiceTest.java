package com.arturcapelossi.agilepm.application.service;

import com.arturcapelossi.agilepm.api.dto.request.LoginRequest;
import com.arturcapelossi.agilepm.api.dto.request.RegisterRequest;
import com.arturcapelossi.agilepm.api.dto.response.AuthResponse;
import com.arturcapelossi.agilepm.domain.model.enums.Role;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.UserEntity;
import com.arturcapelossi.agilepm.infrastructure.persistence.repository.SpringDataUserRepository;
import com.arturcapelossi.agilepm.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private SpringDataUserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);

        jwtService = new JwtService() {
            @Override
            public String generateToken(org.springframework.security.core.userdetails.UserDetails userDetails) {
                return "jwtToken";
            }
        };

        authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authenticationManager);

        registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        userEntity = new UserEntity();
        userEntity.setId(java.util.UUID.randomUUID());
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
        userEntity.setPasswordHash("encodedPassword");
        userEntity.setRole(Role.MEMBER);
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenRegistrationIsSuccessful() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        // jwtService stub returns "jwtToken" automatically

        AuthResponse response = authenticationService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void authenticate_ShouldReturnAuthResponse_WhenAuthenticationIsSuccessful() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        // jwtService stub returns "jwtToken" automatically

        AuthResponse response = authenticationService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}


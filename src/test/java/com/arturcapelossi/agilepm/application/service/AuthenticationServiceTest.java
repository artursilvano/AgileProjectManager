package com.arturcapelossi.agilepm.application.service;

import com.arturcapelossi.agilepm.api.dto.request.LoginRequest;
import com.arturcapelossi.agilepm.api.dto.request.RegisterRequest;
import com.arturcapelossi.agilepm.api.dto.response.AuthResponse;
import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.model.enums.Role;
import com.arturcapelossi.agilepm.domain.repository.UserRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.UserEntity;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.UserMapper;
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
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
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

        authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authenticationManager, userMapper);

        registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        user = new User();
        user.setId(java.util.UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPasswordHash("encodedPassword");
        user.setRole(Role.MEMBER);

        userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPasswordHash(user.getPasswordHash());
        userEntity.setRole(user.getRole());
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenRegistrationIsSuccessful() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toEntity(any(User.class))).thenReturn(userEntity);
        // jwtService stub returns "jwtToken" automatically

        AuthResponse response = authenticationService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticate_ShouldReturnAuthResponse_WhenAuthenticationIsSuccessful() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toEntity(any(User.class))).thenReturn(userEntity);
        // jwtService stub returns "jwtToken" automatically

        AuthResponse response = authenticationService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}

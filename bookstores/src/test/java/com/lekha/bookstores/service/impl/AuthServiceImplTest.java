package com.lekha.bookstores.service.impl;

import com.lekha.bookstores.dto.LoginDto;
import com.lekha.bookstores.entity.User;
import com.lekha.bookstores.exception.ResourceNotFoundException;
import com.lekha.bookstores.repository.UserRepository;
import com.lekha.bookstores.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testLogin_success() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("lekha6777@gmail.com");
        loginDto.setPassword("plainPassword");

        User user = new User();
        user.setEmail("lekha6777@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepo.findByEmail("lekha6777@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("lekha6777@gmail.com")).thenReturn("mocked-jwt-token");

        // Act
        String token = authService.login(loginDto);

        // Assert
        assertEquals("mocked-jwt-token", token);
        verify(jwtUtil, times(1)).generateToken("lekha6777@gmail.com");
    }

    @Test
    void testLogin_userNotFound_throwsException() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("notfound@gmail.com");
        loginDto.setPassword("anyPassword");

        when(userRepo.findByEmail("notfound@gmail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            authService.login(loginDto);
        });

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testLogin_wrongPassword_throwsException() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("lekha6777@gmail.com");
        loginDto.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("lekha6777@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepo.findByEmail("lekha6777@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginDto);
        });

        assertEquals("Invalid password", exception.getMessage());
        verify(jwtUtil, never()).generateToken(anyString());
    }
}
package com.lekha.bookstores.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lekha.bookstores.config.SecurityConfig;
import com.lekha.bookstores.controller.AuthController;
import com.lekha.bookstores.dto.LoginDto;
import com.lekha.bookstores.repository.UserRepository;
import com.lekha.bookstores.security.JwtUtil;
import com.lekha.bookstores.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin_success() throws Exception {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("lekha6777@gmail.com");
        loginDto.setPassword("plainPassword");

        when(authService.login(any(LoginDto.class))).thenReturn("mocked-jwt-token");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.message").value("Login successfully"));
    }
}
package com.lekha.bookstores.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lekha.bookstores.config.SecurityConfig;
import com.lekha.bookstores.controller.UserController;
import com.lekha.bookstores.dto.UserDto;
import com.lekha.bookstores.repository.UserRepository;
import com.lekha.bookstores.security.JwtUtil;
import com.lekha.bookstores.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void testCreateUser_success() throws Exception {
        UserDto inputDto = new UserDto();
        inputDto.setName("lekha");
        inputDto.setEmail("lekha6777@gmail.com");
        inputDto.setPassword("12345");
        inputDto.setRole("USER");
        when(service.saveUser(any(UserDto.class))).thenReturn(inputDto);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("lekha"))
                .andExpect(jsonPath("$.message").value("save user successfully"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetUsers_success() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("lekha");
        dto.setEmail("lekha6777@gmail.com");

        when(service.getAllUsers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("lekha"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetUserById_success() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("lekha");
        dto.setEmail("lekha6777@gmail.com");

        when(service.getUserById(1L)).thenReturn(dto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("lekha"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser_success() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/users/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("user deleted successfully"));
    }
}
package com.lekha.bookstores.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lekha.bookstores.config.SecurityConfig;
import com.lekha.bookstores.controller.ProductController;
import com.lekha.bookstores.dto.ProductDto;
import com.lekha.bookstores.repository.UserRepository;
import com.lekha.bookstores.security.JwtUtil;
import com.lekha.bookstores.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateProduct_asAdmin_success() throws Exception {
        // Arrange
        ProductDto inputDto = new ProductDto();
        inputDto.setName("tv");
        inputDto.setPrice(25000.0);
        inputDto.setQuantity(5);

        when(service.saveProduct(any(ProductDto.class))).thenReturn(inputDto);

        // Act & Assert
        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("tv"))
                .andExpect(jsonPath("$.message").value("product added successfully"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateProduct_asUser_forbidden() throws Exception {
        ProductDto inputDto = new ProductDto();
        inputDto.setName("tv");
        inputDto.setPrice(25000.0);
        inputDto.setQuantity(2);

        // Act & Assert — USER role should NOT be allowed to create products
        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetProducts_asUser_success() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setName("tv");

        when(service.getAllProducts()).thenReturn(List.of(dto));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("tv"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetProductById_success() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setName("tv");

        when(service.getProductById(1L)).thenReturn(dto);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("tv"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateProduct_asAdmin_success() throws Exception {
        // Arrange
        ProductDto inputDto = new ProductDto();
        inputDto.setName("Smart TV");
        inputDto.setPrice(30000.0);
        inputDto.setQuantity(5);

        when(service.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(inputDto);

        mockMvc.perform(put("/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Smart TV"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct_asAdmin_success() throws Exception {
        doNothing().when(service).deleteProduct(1L);

        mockMvc.perform(delete("/products/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("product deleted successfully"));
    }
}
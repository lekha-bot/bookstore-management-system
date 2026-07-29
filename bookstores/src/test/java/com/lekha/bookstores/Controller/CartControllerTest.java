package com.lekha.bookstores.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lekha.bookstores.config.SecurityConfig;
import com.lekha.bookstores.controller.CartController;
import com.lekha.bookstores.dto.CartDto;
import com.lekha.bookstores.repository.UserRepository;
import com.lekha.bookstores.security.JwtUtil;
import com.lekha.bookstores.service.CartService;
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

@WebMvcTest(CartController.class)
@Import(SecurityConfig.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void testAddToCart_success() throws Exception {
        CartDto inputDto = new CartDto();
        inputDto.setUserId(1L);
        inputDto.setProductId(2L);
        inputDto.setQuantity(3);

        when(cartService.addToCart(any(CartDto.class))).thenReturn(inputDto);

        mockMvc.perform(post("/cart")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.productId").value(2))
                .andExpect(jsonPath("$.data.quantity").value(3))
                .andExpect(jsonPath("$.message").value("cart created successfully"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetCart_success() throws Exception {
        CartDto cartItem1 = new CartDto();
        cartItem1.setUserId(1L);
        cartItem1.setProductId(10L);
        cartItem1.setQuantity(2);

        CartDto cartItem2 = new CartDto();
        cartItem2.setUserId(1L);
        cartItem2.setProductId(20L);
        cartItem2.setQuantity(5);

        when(cartService.getCart(1L)).thenReturn(List.of(cartItem1, cartItem2));

        mockMvc.perform(get("/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].productId").value(10))
                .andExpect(jsonPath("$.data[1].quantity").value(5))
                .andExpect(jsonPath("$.message").value("cart fetched"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRemoveFromCart_success() throws Exception {
        doNothing().when(cartService).removeFromCart(5L);

        mockMvc.perform(delete("/cart/5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("cart item deleted"));
    }
}
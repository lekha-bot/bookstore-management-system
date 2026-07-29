package com.lekha.bookstores.service.impl;

import com.lekha.bookstores.dto.CartDto;
import com.lekha.bookstores.entity.Cart;
import com.lekha.bookstores.entity.Product;
import com.lekha.bookstores.entity.User;
import com.lekha.bookstores.exception.ResourceNotFoundException;
import com.lekha.bookstores.repository.CartRepository;
import com.lekha.bookstores.repository.ProductRepository;
import com.lekha.bookstores.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void testAddToCart_success() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(2L);

        CartDto inputDto = new CartDto();
        inputDto.setUserId(1L);
        inputDto.setProductId(2L);
        inputDto.setQuantity(3);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        // Act
        CartDto result = cartService.addToCart(inputDto);

        // Assert
        assertEquals(1L, result.getUserId());
        assertEquals(2L, result.getProductId());
        assertEquals(3, result.getQuantity());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddToCart_userNotFound_throwsException() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        CartDto inputDto = new CartDto();
        inputDto.setUserId(99L);
        inputDto.setProductId(2L);
        inputDto.setQuantity(1);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addToCart(inputDto);
        });

        // productRepository should never be reached since user check fails first
        verify(productRepository, never()).findById(anyLong());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testAddToCart_productNotFound_throwsException() {
        // Arrange
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        CartDto inputDto = new CartDto();
        inputDto.setUserId(1L);
        inputDto.setProductId(99L);
        inputDto.setQuantity(1);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addToCart(inputDto);
        });

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testGetCart_returnsList() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Product product1 = new Product();
        product1.setId(10L);

        Product product2 = new Product();
        product2.setId(20L);

        Cart cart1 = new Cart();
        cart1.setUser(user);
        cart1.setProduct(product1);
        cart1.setQuantity(2);

        Cart cart2 = new Cart();
        cart2.setUser(user);
        cart2.setProduct(product2);
        cart2.setQuantity(5);

        when(cartRepository.findByUserId(1L)).thenReturn(List.of(cart1, cart2));

        // Act
        List<CartDto> result = cartService.getCart(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getProductId());
        assertEquals(5, result.get(1).getQuantity());
    }

    @Test
    void testRemoveFromCart_success() {
        // Act
        cartService.removeFromCart(5L);

        // Assert
        verify(cartRepository, times(1)).deleteById(5L);
    }
}
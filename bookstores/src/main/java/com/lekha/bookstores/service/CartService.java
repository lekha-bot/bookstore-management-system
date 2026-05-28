package com.lekha.bookstores.service;

import com.lekha.bookstores.dto.CartDto;
import java.util.List;

public interface CartService {
    CartDto addToCart(CartDto cartDto);
    List<CartDto> getCart(Long userId);
    void removeFromCart(Long cartId);
}
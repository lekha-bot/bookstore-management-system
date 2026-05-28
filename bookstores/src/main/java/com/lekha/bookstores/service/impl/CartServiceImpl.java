package com.lekha.bookstores.service.impl;

import com.lekha.bookstores.dto.CartDto;
import com.lekha.bookstores.dto.ProductDto;
import com.lekha.bookstores.entity.Cart;
import com.lekha.bookstores.entity.Product;
import com.lekha.bookstores.entity.User;
import com.lekha.bookstores.exception.ResourceNotFoundException;
import com.lekha.bookstores.repository.CartRepository;
import com.lekha.bookstores.repository.ProductRepository;
import com.lekha.bookstores.repository.UserRepository;
import com.lekha.bookstores.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;


    @Override
    public CartDto addToCart(CartDto cartDto){
        User user = userRepository.findById(cartDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(cartDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(cartDto.getQuantity());

        cartRepository.save(cart);
        return mapToDto(cart);
    }

    @Override
    public List<CartDto> getCart(Long userId){
        List<Cart> carts = cartRepository.findByUserId(userId);
        return carts.stream()
                .map(cart -> mapToDto(cart))
                .collect(Collectors.toList());
    }

    @Override
    public void removeFromCart(Long cartId){
        cartRepository.deleteById(cartId);
    }


    private CartDto mapToDto(Cart cart){
        CartDto dto = new CartDto();
        dto.setUserId(cart.getUser().getId());
        dto.setProductId(cart.getProduct().getId());
        dto.setQuantity(cart.getQuantity());
        return dto;
    }
}
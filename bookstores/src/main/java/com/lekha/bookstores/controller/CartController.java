package com.lekha.bookstores.controller;

import com.lekha.bookstores.dto.CartDto;
import com.lekha.bookstores.payload.ApiResponse;
import com.lekha.bookstores.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<CartDto>> addToCart(
            @Valid @RequestBody CartDto cartDto){
        CartDto cart = cartService.addToCart(cartDto);
        ApiResponse<CartDto> response = new ApiResponse<>(
                "success", "cart created successfully", cart);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartDto>>> getCart(
            @PathVariable Long userId){
        List<CartDto> cart = cartService.getCart(userId);
        ApiResponse<List<CartDto>> response = new ApiResponse<>(
                "success", "cart fetched", cart);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<String>> removeFromCart(
            @PathVariable Long cartId){
        cartService.removeFromCart(cartId);
        ApiResponse<String> response = new ApiResponse<>(
                "success", "cart item deleted", null);
        return ResponseEntity.ok(response);
    }
}
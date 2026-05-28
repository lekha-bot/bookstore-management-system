package com.lekha.bookstores.repository;

import com.lekha.bookstores.entity.Cart;
import com.lekha.bookstores.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    List<Cart> findByUserId(Long userId);
}

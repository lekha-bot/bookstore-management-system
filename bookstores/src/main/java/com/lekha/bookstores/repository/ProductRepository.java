package com.lekha.bookstores.repository;

import com.lekha.bookstores.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}

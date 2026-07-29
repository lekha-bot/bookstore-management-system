package com.lekha.bookstores.service.impl;

import com.lekha.bookstores.dto.ProductDto;
import com.lekha.bookstores.entity.Product;
import com.lekha.bookstores.exception.ResourceNotFoundException;
import com.lekha.bookstores.repository.ProductRepository;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testSaveProduct_success() {
        // Arrange
        ProductDto inputDto = new ProductDto();
        inputDto.setName("tv");
        inputDto.setPrice(25000.0);
        inputDto.setQuantity(2);
        inputDto.setDescription("sonyLed Tv 52 inches");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("tv");
        savedProduct.setPrice(25000.0);
        savedProduct.setQuantity(2);
        savedProduct.setDescription("sonyLed Tv 52 inches");

        when(repo.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        ProductDto result = productService.saveProduct(inputDto);

        // Assert
        assertEquals("tv", result.getName());
        assertEquals(2, result.getQuantity());
        assertEquals(25000.0, result.getPrice(), 0.01);
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testGetAllProducts_returnsList() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("tv");
        product1.setPrice(25000.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("phone");
        product2.setPrice(15000.0);

        when(repo.findAll()).thenReturn(List.of(product1, product2));

        // Act
        List<ProductDto> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("tv", result.get(0).getName());
        assertEquals(15000.0, result.get(1).getPrice(), 0.01);
    }

    @Test
    void testGetProductById_found() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("tv");
        product.setPrice(25000.0);

        when(repo.findById(1L)).thenReturn(Optional.of(product));

        // Act
        ProductDto result = productService.getProductById(1L);

        // Assert
        assertEquals("tv", result.getName());
        assertEquals(25000.0, result.getPrice(), 0.01);
    }

    @Test
    void testGetProductById_notFound_throwsException() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(99L);
        });
    }

    @Test
    void testUpdateProduct_success() {
        // Arrange
        Product existing = new Product();
        existing.setId(1L);
        existing.setName("tv");
        existing.setPrice(25000.0);
        existing.setQuantity(2);
        existing.setDescription("old description");

        ProductDto updateDto = new ProductDto();
        updateDto.setName("Smart TV");
        updateDto.setPrice(30000.0);
        updateDto.setQuantity(5);
        updateDto.setDescription("updated description");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Product.class))).thenReturn(existing);

        // Act
        ProductDto result = productService.updateProduct(1L, updateDto);

        // Assert
        assertEquals("Smart TV", result.getName());
        assertEquals(30000.0, result.getPrice(), 0.01);
        assertEquals(5, result.getQuantity());
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_notFound_throwsException() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        ProductDto updateDto = new ProductDto();
        updateDto.setName("Smart TV");
        updateDto.setPrice(30000.0);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(99L, updateDto);
        });
    }

    @Test
    void testDeleteProduct_success() {
        // Arrange
        Product product = new Product();
        product.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(repo, times(1)).delete(product);
    }

    @Test
    void testDeleteProduct_notFound_throwsException() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(99L);
        });
    }
}
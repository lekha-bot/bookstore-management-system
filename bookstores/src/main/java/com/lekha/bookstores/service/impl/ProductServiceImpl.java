package com.lekha.bookstores.service.impl;

import com.lekha.bookstores.dto.ProductDto;
import com.lekha.bookstores.entity.Product;
import com.lekha.bookstores.exception.ResourceNotFoundException;
import com.lekha.bookstores.repository.ProductRepository;
import com.lekha.bookstores.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repo;
    @Override
    public ProductDto saveProduct(ProductDto productDto){

        Product product = mapToEntity(productDto);

        Product savedProduct = repo.save(product);

        return mapToDto(savedProduct);
    }
    @Override
    public List<ProductDto> getAllProducts(){

        List<Product> products = repo.findAll();

        return products.stream()
                .map(this::mapToDto)
                .toList();
    }
    @Override
    public  ProductDto getProductById(Long id){
        Product product = repo.findById(id).orElseThrow(()->new ResourceNotFoundException("product not found"));
        return mapToDto(product);
    }
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto){

        Product existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        existing.setName(productDto.getName());
        existing.setQuantity(productDto.getQuantity());
        existing.setPrice(productDto.getPrice());
        existing.setDescription(productDto.getDescription());

        Product savedProduct = repo.save(existing);

        return mapToDto(savedProduct);
    }
    @Override
    public void deleteProduct(Long id){

        Product product = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        repo.delete(product);
    }

    public ProductDto mapToDto(Product product){
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setDescription(product.getDescription());
        return dto;
    }

    public Product mapToEntity(ProductDto dto){
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setDescription(dto.getDescription());
        return product;
    }
}

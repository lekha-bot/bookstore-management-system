package com.lekha.bookstores.controller;

import com.lekha.bookstores.dto.ProductDto;
import com.lekha.bookstores.payload.ApiResponse;
import com.lekha.bookstores.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody  ProductDto productDto){
        ProductDto product= service.saveProduct(productDto);
        ApiResponse<ProductDto> response =new ApiResponse<>("success","product added successfully",product);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProducts(){
        List<ProductDto> products = service.getAllProducts();
        ApiResponse<List<ProductDto>> response = new ApiResponse<>("success","all product fetched",products);
        return  ResponseEntity.ok(response);

    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id){
        ProductDto product= service.getProductById(id);
        ApiResponse<ProductDto> response =new ApiResponse<>("success","product fetched",product);
        return ResponseEntity.ok(response);

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@PathVariable Long id,
                                   @Valid @RequestBody ProductDto productDto){
        ProductDto product= service.updateProduct(id, productDto);
        ApiResponse<ProductDto> response=new ApiResponse<>("success","product updated",product);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
        ApiResponse<String> response=new ApiResponse<>("success","product deleted successfully",null);
        return ResponseEntity.ok(response);
    }
}

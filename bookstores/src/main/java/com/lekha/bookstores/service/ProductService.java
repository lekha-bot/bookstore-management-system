package com.lekha.bookstores.service;

import com.lekha.bookstores.dto.ProductDto;

import java.util.List;

public interface ProductService {
     ProductDto saveProduct(ProductDto productDto);

     List<ProductDto> getAllProducts();


     ProductDto getProductById(Long id);

     ProductDto updateProduct(Long id,ProductDto productDto);
     void deleteProduct(Long id);


}

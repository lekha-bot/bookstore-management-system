package com.lekha.bookstores.dto;

import com.lekha.bookstores.entity.Product;
import com.lekha.bookstores.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    @NotNull(message = "user_id should not be blank")
    private Long userId;
    @NotNull(message = "product_id should not be blank")
    private Long productId;
    @Min(value=1,message ="Quantity should be minimum 1")
    private int quantity;
}

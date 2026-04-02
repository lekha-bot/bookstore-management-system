package com.lekha.bookstores.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private long id;
    @NotBlank(message = "product name should not be blank")
    private String name;
    @Min(value = 1)
    private int quantity;
    @DecimalMin(value = "0.0")
    private Double price;
    private String description;
}

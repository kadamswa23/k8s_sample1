package com.example.springjpa.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @NotNull
    @Max(value = 50)
    @Min(value = 5)
    private String code;
    @NotNull
    @Max(value = 100)
    private String name;
    private String description;
    private String category;

    private double price;
}

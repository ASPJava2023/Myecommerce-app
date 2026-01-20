package com.ecommerce.productservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255)
    private String name;

    private String description;

    @NotBlank(message = "SKU is required")
    @Size(max = 100)
    private String sku;

    private String brand;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @DecimalMin(value = "0", message = "Discount cannot be negative")
    @DecimalMax(value = "100", message = "Discount cannot exceed 100%")
    private BigDecimal discountPercentage;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private String imageUrl;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Boolean isFeatured;
}

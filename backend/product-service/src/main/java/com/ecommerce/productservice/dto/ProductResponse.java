package com.ecommerce.productservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String sku;
    private String brand;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private BigDecimal discountedPrice;
    private Integer stockQuantity;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean inStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

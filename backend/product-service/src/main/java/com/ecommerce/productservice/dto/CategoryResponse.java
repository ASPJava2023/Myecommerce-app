package com.ecommerce.productservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isActive;
    private Integer displayOrder;
    private Long productCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

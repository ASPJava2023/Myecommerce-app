package com.ecommerce.productservice.service;

import com.ecommerce.common.dto.PagedResponse;
import com.ecommerce.productservice.dto.*;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    ProductResponse getProductById(Long id);

    PagedResponse<ProductResponse> getAllProducts(Pageable pageable);

    PagedResponse<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);

    PagedResponse<ProductResponse> searchProducts(String search, Pageable pageable);

    PagedResponse<ProductResponse> getFeaturedProducts(Pageable pageable);

    void deleteProduct(Long id);

    void updateStock(Long id, Integer quantity);
}

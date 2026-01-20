package com.ecommerce.productservice.service.impl;

import com.ecommerce.common.dto.PagedResponse;
import com.ecommerce.common.exception.*;
import com.ecommerce.productservice.dto.*;
import com.ecommerce.productservice.entity.*;
import com.ecommerce.productservice.repository.*;
import com.ecommerce.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getName());

        if (productRepository.findBySku(request.getSku()).isPresent()) {
            throw new ResourceAlreadyExistsException("Product", "SKU", request.getSku());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .sku(request.getSku())
                .brand(request.getBrand())
                .price(request.getPrice())
                .discountPercentage(request.getDiscountPercentage())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .category(category)
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .isActive(true)
                .build();

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
        product.setIsFeatured(request.getIsFeatured());

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsActiveTrue(pageable);
        return mapToPagedResponse(products);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        return mapToPagedResponse(products);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> searchProducts(String search, Pageable pageable) {
        Page<Product> products = productRepository.searchProducts(search, pageable);
        return mapToPagedResponse(products);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> getFeaturedProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsFeaturedTrueAndIsActiveTrue(pageable);
        return mapToPagedResponse(products);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setStockQuantity(quantity);
        productRepository.save(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .brand(product.getBrand())
                .price(product.getPrice())
                .discountPercentage(product.getDiscountPercentage())
                .discountedPrice(product.getDiscountedPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .isActive(product.getIsActive())
                .isFeatured(product.getIsFeatured())
                .inStock(product.isInStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private PagedResponse<ProductResponse> mapToPagedResponse(Page<Product> page) {
        return PagedResponse.of(page, page.getContent().stream()
                .map(this::mapToResponse)
                .toList());
    }
}

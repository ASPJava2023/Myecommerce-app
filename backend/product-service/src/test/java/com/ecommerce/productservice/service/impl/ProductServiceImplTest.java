package com.ecommerce.productservice.service.impl;

import com.ecommerce.common.dto.PagedResponse;
import com.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;
    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic items")
                .isActive(true)
                .build();

        productRequest = ProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .sku("TEST-SKU-001")
                .brand("Test Brand")
                .price(new BigDecimal("99.99"))
                .discountPercentage(new BigDecimal("10"))
                .stockQuantity(100)
                .imageUrl("http://example.com/image.jpg")
                .categoryId(1L)
                .isFeatured(true)
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .sku("TEST-SKU-001")
                .brand("Test Brand")
                .price(new BigDecimal("99.99"))
                .discountPercentage(new BigDecimal("10"))
                .stockQuantity(100)
                .imageUrl("http://example.com/image.jpg")
                .category(testCategory)
                .isFeatured(true)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should successfully create a new product")
    void testCreateProduct_Success() {
        // Given
        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        ProductResponse response = productService.createProduct(productRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Test Product");
        assertThat(response.getSku()).isEqualTo("TEST-SKU-001");
        assertThat(response.getPrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(response.getCategoryId()).isEqualTo(1L);
        assertThat(response.getIsFeatured()).isTrue();

        verify(productRepository).findBySku("TEST-SKU-001");
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceAlreadyExistsException when SKU already exists")
    void testCreateProduct_DuplicateSku() {
        // Given
        when(productRepository.findBySku(anyString())).thenReturn(Optional.of(testProduct));

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(productRequest))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("TEST-SKU-001");

        verify(productRepository).findBySku("TEST-SKU-001");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category not found")
    void testCreateProduct_CategoryNotFound() {
        // Given
        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(productRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should successfully update an existing product")
    void testUpdateProduct_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        ProductResponse response = productService.updateProduct(1L, productRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent product")
    void testUpdateProduct_ProductNotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.updateProduct(1L, productRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should successfully get product by ID")
    void testGetProductById_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When
        ProductResponse response = productService.getProductById(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test Product");
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product not found by ID")
    void testGetProductById_NotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getProductById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product");
    }

    @Test
    @DisplayName("Should successfully get all products with pagination")
    void testGetAllProducts_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);
        when(productRepository.findByIsActiveTrue(pageable)).thenReturn(productPage);

        // When
        PagedResponse<ProductResponse> response = productService.getAllProducts(pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getPageNumber()).isEqualTo(0);
        verify(productRepository).findByIsActiveTrue(pageable);
    }

    @Test
    @DisplayName("Should successfully get products by category")
    void testGetProductsByCategory_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);
        when(productRepository.findByCategoryIdAndIsActiveTrue(1L, pageable)).thenReturn(productPage);

        // When
        PagedResponse<ProductResponse> response = productService.getProductsByCategory(1L, pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        verify(productRepository).findByCategoryIdAndIsActiveTrue(1L, pageable);
    }

    @Test
    @DisplayName("Should successfully search products")
    void testSearchProducts_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);
        when(productRepository.searchProducts("test", pageable)).thenReturn(productPage);

        // When
        PagedResponse<ProductResponse> response = productService.searchProducts("test", pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        verify(productRepository).searchProducts("test", pageable);
    }

    @Test
    @DisplayName("Should successfully get featured products")
    void testGetFeaturedProducts_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);
        when(productRepository.findByIsFeaturedTrueAndIsActiveTrue(pageable)).thenReturn(productPage);

        // When
        PagedResponse<ProductResponse> response = productService.getFeaturedProducts(pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getIsFeatured()).isTrue();
        verify(productRepository).findByIsFeaturedTrueAndIsActiveTrue(pageable);
    }

    @Test
    @DisplayName("Should successfully soft delete product")
    void testDeleteProduct_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent product")
    void testDeleteProduct_NotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should successfully update product stock")
    void testUpdateStock_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        productService.updateStock(1L, 50);

        // Then
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating stock of non-existent product")
    void testUpdateStock_NotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.updateStock(1L, 50))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

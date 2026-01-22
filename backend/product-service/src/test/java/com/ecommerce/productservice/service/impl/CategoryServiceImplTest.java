package com.ecommerce.productservice.service.impl;

import com.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.dto.CategoryRequest;
import com.ecommerce.productservice.dto.CategoryResponse;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Unit Tests")
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryRequest categoryRequest;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        categoryRequest = CategoryRequest.builder()
                .name("Electronics")
                .description("Electronic items")
                .imageUrl("http://example.com/category.jpg")
                .displayOrder(1)
                .build();

        testCategory = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic items")
                .imageUrl("http://example.com/category.jpg")
                .displayOrder(1)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should successfully create a new category")
    void testCreateCategory_Success() {
        // Given
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // When
        CategoryResponse response = categoryService.createCategory(categoryRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Electronics");
        assertThat(response.getDescription()).isEqualTo("Electronic items");
        assertThat(response.getDisplayOrder()).isEqualTo(1);
        assertThat(response.getIsActive()).isTrue();

        verify(categoryRepository).existsByName("Electronics");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw ResourceAlreadyExistsException when category name already exists")
    void testCreateCategory_DuplicateName() {
        // Given
        when(categoryRepository.existsByName(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> categoryService.createCategory(categoryRequest))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Electronics");

        verify(categoryRepository).existsByName("Electronics");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should set default display order when not provided")
    void testCreateCategory_DefaultDisplayOrder() {
        // Given
        categoryRequest.setDisplayOrder(null);
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // When
        CategoryResponse response = categoryService.createCategory(categoryRequest);

        // Then
        assertThat(response).isNotNull();
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should successfully update an existing category")
    void testUpdateCategory_Success() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // When
        CategoryResponse response = categoryService.updateCategory(1L, categoryRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent category")
    void testUpdateCategory_NotFound() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.updateCategory(1L, categoryRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should successfully get category by ID")
    void testGetCategoryById_Success() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.countByCategoryId(1L)).thenReturn(5L);

        // When
        CategoryResponse response = categoryService.getCategoryById(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Electronics");
        assertThat(response.getProductCount()).isEqualTo(5L);
        verify(categoryRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category not found by ID")
    void testGetCategoryById_NotFound() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.getCategoryById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");
    }

    @Test
    @DisplayName("Should successfully get all active categories")
    void testGetAllCategories_Success() {
        // Given
        Category category2 = Category.builder()
                .id(2L)
                .name("Books")
                .description("Book items")
                .displayOrder(2)
                .isActive(true)
                .build();

        when(categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc())
                .thenReturn(List.of(testCategory, category2));
        when(productRepository.countByCategoryId(anyLong())).thenReturn(5L);

        // When
        List<CategoryResponse> responses = categoryService.getAllCategories();

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("Electronics");
        assertThat(responses.get(1).getName()).isEqualTo("Books");
        verify(categoryRepository).findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    @Test
    @DisplayName("Should return empty list when no active categories exist")
    void testGetAllCategories_Empty() {
        // Given
        when(categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc())
                .thenReturn(List.of());

        // When
        List<CategoryResponse> responses = categoryService.getAllCategories();

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("Should successfully soft delete category")
    void testDeleteCategory_Success() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // When
        categoryService.deleteCategory(1L);

        // Then
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent category")
    void testDeleteCategory_NotFound() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(categoryRepository, never()).save(any(Category.class));
    }
}

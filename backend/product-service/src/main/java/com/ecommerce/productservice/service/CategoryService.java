package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.*;
import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    void deleteCategory(Long id);
}

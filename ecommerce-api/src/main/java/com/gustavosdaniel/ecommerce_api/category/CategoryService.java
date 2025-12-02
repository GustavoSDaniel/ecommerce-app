package com.gustavosdaniel.ecommerce_api.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    Page<CategoryResponse> getCategories(Pageable pageable);

    CategoryResponse getCategoryById(Integer id);

    List<CategoryResponse> searchCategories(String name);

    CategoryUpdateResponse updateCategory(Integer id, CategoryUpdateRequest request);

    void deleteCategory(Integer id);
}

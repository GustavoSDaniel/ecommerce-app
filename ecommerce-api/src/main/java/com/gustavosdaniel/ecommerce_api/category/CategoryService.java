package com.gustavosdaniel.ecommerce_api.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    Page<CategoryResponse> getCategories(Pageable pageable);

    void deleteCategory(Integer id);
}

package com.gustavosdaniel.ecommerce_api.category;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    void deleteCategory(Integer id);
}

package com.gustavosdaniel.ecommerce_api.category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryRequest request) {
        if(request == null) return null;

        return new Category(
              request.name(),
              request.description()
        );
    }

    public CategoryResponse toCategoryResponse(Category category) {
        if(category == null) return null;

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedBy()
        );
    }
}

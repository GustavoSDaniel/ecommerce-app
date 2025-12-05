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

    public void toUpdateCategory(CategoryUpdateRequest request, Category category) {

        if (request == null) return;

        if (request.name() != null && !request.name().isBlank()) {

            category.setName(request.name());
        }

        if (request.description() != null && !request.description().isBlank()) {

            category.setDescription(request.description());
        }

    }

    public CategoryUpdateResponse toUpdateCategoryResponse(Category category) {
        if(category == null) return null;

        return new CategoryUpdateResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getLastModifiedBy()
        );
    }
}

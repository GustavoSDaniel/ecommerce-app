package com.gustavosdaniel.ecommerce_api.category;

public record CategoryResponse(

        Integer id,
        String name,
        String description,
        String createdBy) {
}

package com.gustavosdaniel.ecommerce_api.category;

public record CategoryUpdateResponse(

        Integer id,
        String name,
        String description,
        String lastModifiedBy) {
}

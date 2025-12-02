package com.gustavosdaniel.ecommerce_api.product;


import java.math.BigDecimal;

public record ProductResponse(

        String categoryName,

        Long id,

        String name,

        String description,

        MeasureUnit measureUnit,

        BigDecimal availableQuantity,

        BigDecimal price,

        String createdBy) {
}

package com.gustavosdaniel.ecommerce_api.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductUpdateRequest(

        @Size(min = 3, message = "O nome do Produto deve ter pelo menos 3 caracteres")
        String name,

        @Size(min = 10, message = "O nome do Produto deve ter pelo menos 10 caracteres")
        String description,

        MeasureUnit measureUnit,

        @Positive(message = "A quantidade disponível deve ser um valor positivo")
        @DecimalMin(value = "0.001",
                message = "A quantidade deve ser maior que zero (para unidades fracionadas)")
        BigDecimal availableQuantity,

        @Positive(message = "O preço deve ser um valor positivo")
        BigDecimal price

){
}

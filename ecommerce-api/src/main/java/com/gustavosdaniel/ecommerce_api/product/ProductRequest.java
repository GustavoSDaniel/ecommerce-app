package com.gustavosdaniel.ecommerce_api.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest (

        @NotBlank(message = "O campo nome do Produto é obrigatório")
        @Size(min = 3, message = "O nome do Produto deve ter pelo menos 3 caracteres")
        String name,

        @NotBlank(message = "O campo descrição do Produto é obrigatório")
        @Size(min = 10, message = "O nome do Produto deve ter pelo menos 10 caracteres")
        String description,

        @NotNull(message = "A unidade de medida é obrigatória")
        MeasureUnit measureUnit,

        @NotNull(message = "A quantidade disponível é obrigatória")
        @PositiveOrZero(message = "A quantidade disponível deve ser um valor positivo")
        BigDecimal availableQuantity,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser um valor positivo")
        BigDecimal price

){
}

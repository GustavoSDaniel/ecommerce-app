package com.gustavosdaniel.ecommerce_api.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record StockUpdateRequest(

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser positiva")
        BigDecimal quantity,
        @NotNull(message = "O tipo de operação é obrigatório (ADD, REMOVE, SET)")
        StockOperationType stockOperationType
) {
}

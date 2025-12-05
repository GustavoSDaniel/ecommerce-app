package com.gustavosdaniel.ecommerce_api.product;

import java.math.BigDecimal;

public record StockUpdateResponse(

        Long productId,
        String name,
        StockOperationType operation,
        BigDecimal quantityMovimentada,
        BigDecimal newQQuantidade
) {
}

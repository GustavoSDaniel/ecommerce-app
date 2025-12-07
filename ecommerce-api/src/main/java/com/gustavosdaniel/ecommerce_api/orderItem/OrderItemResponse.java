package com.gustavosdaniel.ecommerce_api.orderItem;

import java.math.BigDecimal;

public record OrderItemResponse(

        Long productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subTotal) {
}

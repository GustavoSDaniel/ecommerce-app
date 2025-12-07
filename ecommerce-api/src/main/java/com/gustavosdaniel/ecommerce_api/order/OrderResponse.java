package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.orderItem.OrderItemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID id,
        String reference,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderItemResponse> itens,
        OrderUserResponse user,
        LocalDateTime createdAt
) {
}

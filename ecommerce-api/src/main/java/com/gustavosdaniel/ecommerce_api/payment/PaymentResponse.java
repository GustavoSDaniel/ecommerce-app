package com.gustavosdaniel.ecommerce_api.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse (

       UUID id,
       String reference,
       BigDecimal amount,
       PaymentMethod method,
       PaymentStatus status,
       LocalDateTime confirmedAt,
       String failureReason,
       UUID orderId,
       String orderReference,
       LocalDateTime createdAt,
       LocalDateTime updatedAt
) {
}

package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.order.Order;

import java.util.Optional;
import java.util.UUID;


public interface PaymentService {

    Payment processPayment(Order order, PaymentRequest paymentRequest);

    Optional<PaymentResponse>  getPaymentByOrderId(UUID orderId, UUID userId);
}

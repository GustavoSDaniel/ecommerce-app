package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;


public interface PaymentService {

    Payment processPayment(Order order, PaymentRequest paymentRequest);

    Page<PaymentResponse> getPaymentByUserId(UUID userId, Pageable pageable);

    Optional<PaymentResponse> getPaymentById(UUID id);

    Optional<PaymentResponse>  getPaymentByOrderId(UUID orderId, UUID userId);
}

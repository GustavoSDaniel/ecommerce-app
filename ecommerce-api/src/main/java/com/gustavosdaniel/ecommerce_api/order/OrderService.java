package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.payment.PaymentRequest;
import com.gustavosdaniel.ecommerce_api.payment.PaymentResponse;
import com.gustavosdaniel.ecommerce_api.product.InsuficienteStockException;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionAddAndRemove;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(UUID userID, OrderRequest orderRequest)
            throws InsuficienteStockException, StockOperationExceptionAddAndRemove, StockOperationExceptionSet;

    Page<OrderResponse> getAllOrders(Pageable pageable);

    OrderResponse getOrderById(UUID orderId);

    Page<OrderResponse> getOrdersByUserId(UUID userId, Pageable pageable);

    Page<OrderResponse> getOrderByStatus(OrderStatus status, Pageable pageable);

    Page<OrderResponse> getOrdersByUserAndStatus(UUID userId, OrderStatus status, Pageable pageable);

    Optional<OrderResponse> searchOrdersByReference(UUID userId, String reference);

    PaymentResponse confirmPayment(UUID orderId, PaymentRequest paymentRequest);

    void shippedOrder(UUID orderId);

    void deliveredOrder(UUID orderId);

    void cancelOrder(UUID orderId, UUID userId)
            throws StockOperationExceptionAddAndRemove, StockOperationExceptionSet, InsuficienteStockException;

}

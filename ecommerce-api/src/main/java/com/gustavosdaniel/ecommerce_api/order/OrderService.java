package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.product.InsuficienteStockException;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionAddAndRemove;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(UUID userID, OrderRequest orderRequest)
            throws InsuficienteStockException, StockOperationExceptionAddAndRemove, StockOperationExceptionSet;

    Page<OrderResponse> getAllOrders(Pageable pageable);

    OrderResponse getOrderById(UUID orderId);

    Page<OrderResponse> getOrdersByUserId(UUID userId, Pageable pageable);

    Page<OrderResponse> getOrderByStatus(OrderStatus status, Pageable pageable);


}

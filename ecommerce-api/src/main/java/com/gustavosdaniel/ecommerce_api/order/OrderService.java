package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.product.InsuficienteStockException;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionAddAndRemove;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionSet;

import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(UUID userID, OrderRequest orderRequest)
            throws InsuficienteStockException, StockOperationExceptionAddAndRemove, StockOperationExceptionSet;

    OrderResponse getOrderById(UUID orderId);
}

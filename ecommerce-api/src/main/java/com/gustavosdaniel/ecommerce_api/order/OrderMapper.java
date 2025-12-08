package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.orderItem.OrderItem;
import com.gustavosdaniel.ecommerce_api.orderItem.OrderItemResponse;
import com.gustavosdaniel.ecommerce_api.user.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {

        if(order == null) {
            return null;
        }

        List<OrderItemResponse> itens = order.getOrderItems().stream().map(this::toOrderItemResponse)
                .toList();

        OrderUserResponse user = null;

        if(order.getUser() != null) {

            user = new OrderUserResponse(
                    order.getUser().getId(),
                    order.getUser().getUsername(),
                    order.getUser().getEmail());
        }

        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                itens,
                user,
                order.getCreatedAt()
        );
    }

    private OrderItemResponse toOrderItemResponse(OrderItem orderItem) {

        if(orderItem == null) {
            return null;
        }

        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getName(),
                orderItem.getUnitPrice(),
                orderItem.getQuantity(),
                orderItem.subTotal()
        );
    }


}

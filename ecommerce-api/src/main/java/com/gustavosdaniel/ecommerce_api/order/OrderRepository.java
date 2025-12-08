package com.gustavosdaniel.ecommerce_api.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);

}

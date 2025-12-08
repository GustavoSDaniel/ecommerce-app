package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.orderItem.OrderItem;
import com.gustavosdaniel.ecommerce_api.orderItem.OrderItemRequest;
import com.gustavosdaniel.ecommerce_api.product.*;
import com.gustavosdaniel.ecommerce_api.user.User;
import com.gustavosdaniel.ecommerce_api.user.UserNotFoundException;
import com.gustavosdaniel.ecommerce_api.user.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    @Override
    @Transactional
    public OrderResponse createOrder(UUID userID, OrderRequest orderRequest) throws InsuficienteStockException,
            StockOperationExceptionAddAndRemove, StockOperationExceptionSet {

        log.info("Creating order");

        User user = userRepository.findById(userID).orElseThrow(UserNotFoundException::new);

        Order newOrder = new Order();
        newOrder.setUser(user);

        for (OrderItemRequest item : orderRequest.itens()) {

            Product product = productRepository.findById(item.productId())
                    .orElseThrow(ProductNameExistsException::new);

            BigDecimal quantityProductRequested = BigDecimal.valueOf(item.quantity());

            if (product.getAvailableQuantity().compareTo(quantityProductRequested) < 0) {

                throw new InsuficienteStockException();
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.quantity());
            orderItem.setUnitPrice(product.getPrice());


            newOrder.addOrderItem(orderItem);

            product.handleStockOperation(quantityProductRequested, StockOperationType.REMOVE);

            productRepository.save(product);

        }

        Order saveOrder = orderRepository.save(newOrder);

        log.info("Pedido criado com sucesso. ID: {} | Referência: {} | Total: {}",
                newOrder.getId(), newOrder.getReference(), newOrder.getTotalAmount());

        return orderMapper.toOrderResponse(saveOrder);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {

        log.info("Getting orders");

        Page<Order> orders = orderRepository.findAll(pageable);

        if (orders.isEmpty()) {

            log.info("No orders found");
        }

        log.info("Retornando {} orders  com sucesso", orders.getTotalElements());

        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public OrderResponse getOrderById(UUID orderId) {

        log.info("Buscando order pelo ID");

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        log.info("Retornando order com ID: {}", orderId);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByUserId(UUID userId, Pageable pageable) {

        log.info("Buscando orders pelo ID do usuario {}", userId);

        Page<Order> orders = orderRepository.findByUserId(userId, pageable);

        if (orders.isEmpty()) {

            log.info("Nenhum order encontrado desse usuario {}", userId);
        }

        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<OrderResponse> getOrderByStatus(OrderStatus status, Pageable pageable) {

        log.info("Buscando orders pelo status {}", status);

        Page<Order> orders = orderRepository.findByOrderStatus(status, pageable);

        if (orders.isEmpty()) {

            log.info("Nenhum order encontrado com esse status {}", status);
        }

        log.info("Retornando {} pedidos encontrados com status {}", orders.getTotalElements(), status);

        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByUserAndStatus(UUID userId, OrderStatus status, Pageable pageable) {

        log.info("Buscando orders pelo status {}, do user {}", status, userId);

        Page<Order> orders;

        if (status == null) {

            orders = orderRepository.findByUserId(userId, pageable);

        } else {

            orders = orderRepository.findByUserIdAndOrderStatus(userId, status, pageable);
        }

        log.info("Retornando {} pedidos encontrados com status {} para o suauario {}",
                orders.getTotalElements(), status, userId);

        return orders.map(orderMapper::toOrderResponse);

    }
}

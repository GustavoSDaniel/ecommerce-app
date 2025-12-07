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
}

package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.orderItem.OrderItem;
import com.gustavosdaniel.ecommerce_api.orderItem.OrderItemRequest;
import com.gustavosdaniel.ecommerce_api.product.*;
import com.gustavosdaniel.ecommerce_api.user.User;
import com.gustavosdaniel.ecommerce_api.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Nested
    @DisplayName("Should create order with sucesso")
    class ShouldCreateOrderWithSucesso {

        @Test
        void shouldCreateOrderWithSucesso() throws StockOperationExceptionAddAndRemove, StockOperationExceptionSet, InsuficienteStockException {

            UUID userId = UUID.randomUUID();
            Long productId = 1L;
            Integer quantityItensProduct = 3;
            UUID orderId = UUID.randomUUID();
            String reference = "REFERENCE";


            User user = new User("Gustavo", "senha123", "gustavosdaniel.com");
            ReflectionTestUtils.setField(user, "id", userId);

            Product product = new Product("Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5), BigDecimal.valueOf(40.00));
            ReflectionTestUtils.setField(product, "id", productId);

            OrderItemRequest itens = new OrderItemRequest(productId, quantityItensProduct);

            List<OrderItemRequest> itensList =  List.of(itens);

            OrderRequest request = new OrderRequest(itensList);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(productRepository.save(any(Product.class))).thenReturn(product);
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
                Order orderToSave = invocation.getArgument(0);
                ReflectionTestUtils.setField(orderToSave, "id", orderId);

                return orderToSave;
            });

            OrderResponse response = new OrderResponse(
                    orderId,
                    reference,
                    BigDecimal.valueOf(120.00),
                    OrderStatus.CREATED, List.of(),
                    null,
                    LocalDateTime.now());

            when(orderMapper.toOrderResponse(any(Order.class))).thenReturn(response);

            OrderResponse output = orderService.createOrder(userId, request);

            assertNotNull(output);
            assertEquals(orderId, response.id());

            verify(userRepository).findById(userId);
            verify(productRepository).findById(productId);
            verify(orderRepository).save(any(Order.class));

        }
    }

    @Nested
    @DisplayName("Should order with sucesso by ID")
    class ShouldOrderWithSucesso {

        @Test
        void shouldOrderWithSucessoById() {

            UUID orderId = UUID.randomUUID();
            String reference = "REFERENCE";

            Order order = new Order();
            ReflectionTestUtils.setField(order, "id", orderId);

            OrderResponse response = new OrderResponse(
                    orderId,
                    reference,
                    BigDecimal.valueOf(120.00),
                    OrderStatus.CREATED, List.of(),
                    null,
                    LocalDateTime.now());


            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMapper.toOrderResponse(any(Order.class))).thenReturn(response);

            OrderResponse output = orderService.getOrderById(orderId);

            assertNotNull(output);
            assertEquals(orderId, output.id());

            verify(orderRepository).findById(orderId);
            verify(orderMapper).toOrderResponse(any(Order.class));
        }

    }

    @Nested
    @DisplayName("Should order to userId with sucesso")
    class ShouldOrderToUserIdWithSucesso {

        @Test
        void shouldOrderToUserIdWithSucesso() {

            UUID userId = UUID.randomUUID();
            UUID orderId = UUID.randomUUID();
            String reference = "REFERENCE";

            Pageable pageable = PageRequest.of(0, 1);

            Order order = new Order();
            ReflectionTestUtils.setField(order, "id", orderId);

            List<Order> orderList = Arrays.asList(order);

            Page<Order> orders = new PageImpl<>(orderList, pageable, orderList.size());

            OrderResponse response = new OrderResponse(
                    orderId,
                    reference,
                    BigDecimal.valueOf(120.00),
                    OrderStatus.CREATED, List.of(),
                    null,
                    LocalDateTime.now());


            when(orderRepository.findByUserId(userId, pageable)).thenReturn(orders);
            when(orderMapper.toOrderResponse(any(Order.class))).thenReturn(response);

            Page<OrderResponse> output = orderService.getOrdersByUserId(userId, pageable);

            assertNotNull(output);
            assertEquals(orderId, response.id());

            verify(orderRepository).findByUserId(userId, pageable);
            verify(orderMapper).toOrderResponse(any(Order.class));


        }
    }

    @Nested
    @DisplayName("Should all orders with sucesso")
    class ShouldAllOrdersWithSucesso {

        @Test
        void shouldAllOrdersWithSucesso() {

            UUID orderId = UUID.randomUUID();
            UUID orderId2 = UUID.randomUUID();
            String reference = "REFERENCE";
            String reference2 = "REFERENCE";

            Order order = new Order();
            ReflectionTestUtils.setField(order, "id", orderId);

            Order order2 = new Order();
            ReflectionTestUtils.setField(order2, "id", orderId2);

            Pageable pageable = PageRequest.of(0, 10);

            List<Order> orderList = Arrays.asList(order, order2);

            Page<Order> orders = new PageImpl<>(orderList, pageable, orderList.size());

            OrderResponse response = new OrderResponse(
                    orderId,
                    reference,
                    BigDecimal.valueOf(120.00),
                    OrderStatus.CREATED, List.of(),
                    null,
                    LocalDateTime.now());

            OrderResponse response2 = new OrderResponse(
                    orderId,
                    reference2,
                    BigDecimal.valueOf(120.00),
                    OrderStatus.CREATED, List.of(),
                    null,
                    LocalDateTime.now());

            when(orderRepository.findAll(pageable)).thenReturn(orders);
            when(orderMapper.toOrderResponse(order)).thenReturn(response);
            when(orderMapper.toOrderResponse(order2)).thenReturn(response2);

            Page<OrderResponse> output = orderService.getAllOrders(pageable);

            assertNotNull(output);
            assertEquals(2, output.getTotalElements());
            assertEquals(orderId, output.getContent().get(0).id());

            verify(orderRepository, times(1)).findAll(pageable);


        }
    }



}
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
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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



}
package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.order.Order;

import com.gustavosdaniel.ecommerce_api.order.OrderRepository;
import com.gustavosdaniel.ecommerce_api.order.OrderStatus;
import com.gustavosdaniel.ecommerce_api.user.User;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentGatewaySimulateService paymentGatewaySimulateService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Nested
    @DisplayName("Should processo payment with sucesso")
    class ShouldProcessPaymentWithSucesso {

        @Test
        void shouldProcessPaymentWithSucesso() {


        }
    }

    @Nested
    @DisplayName("Should payment for orderId with sucesso")
    class ShouldPayPaymentForOrderIdWithSucesso {

        @Test
        void shouldPayPaymentForOrderIdWithSucesso() {

            UUID orderId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();

            User user = new User();
            ReflectionTestUtils.setField(user, "id", userId);

            Order order = new Order();
            ReflectionTestUtils.setField(order, "id", orderId);
            order.setUser(user);

            Payment payment = new Payment();
            payment.setOrder(order);

            PaymentResponse response = new PaymentResponse(
                    UUID.randomUUID(),
                    "COMPLETED",
                    BigDecimal.valueOf(120.00),
                    PaymentMethod.PIX,
                    PaymentStatus.COMPLETED,
                    LocalDateTime.now(),
                    "Não falhou",
                    orderId,
                    "referencia do gateway",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            when(paymentRepository.findByOrder_IdAndOrder_User_Id(orderId, userId)).thenReturn(Optional.of(payment));
            when(paymentMapper.toPaymentResponse(payment)).thenReturn(response);

            Optional<PaymentResponse>  output = paymentService.getPaymentByOrderId(orderId, userId);

            assertNotNull(output);
            assertEquals(response, output.get());

            verify(paymentRepository).findByOrder_IdAndOrder_User_Id(orderId, userId);
            verify(paymentMapper).toPaymentResponse(payment);

        }
    }

    @Nested
    @DisplayName("Should payment by id with sucesso")
    class ShouldPayPaymentByIdWithSucesso {

        @Test
        void shouldPayPaymentByIdWithSucesso() {

            UUID paymentId = UUID.randomUUID();
            UUID orderId = UUID.randomUUID();

            Payment payment = new Payment();
            ReflectionTestUtils.setField(payment, "id", paymentId);

            PaymentResponse response = new PaymentResponse(

                    UUID.randomUUID(),
                    "COMPLETED",
                    BigDecimal.valueOf(120.00),
                    PaymentMethod.PIX,
                    PaymentStatus.COMPLETED,
                    LocalDateTime.now(),
                    "Não falhou",
                    orderId,
                    "referencia do gateway",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
            when(paymentMapper.toPaymentResponse(payment)).thenReturn(response);

            Optional<PaymentResponse> output = paymentService.getPaymentById(paymentId);

            assertNotNull(output);
            assertEquals(response, output.get());

            verify(paymentRepository).findById(paymentId);
            verify(paymentMapper).toPaymentResponse(payment);

        }
    }

    @Nested
    @DisplayName("Should pyments for userId with sucesso")
    class ShouldPayPaymentForUserIdWithSucesso {

        @Test
        void shouldPayPaymentForUserIdWithSucesso() {

            UUID userId = UUID.randomUUID();
            UUID orderId = UUID.randomUUID();
            UUID paymentId = UUID.randomUUID();

            Pageable pageable = PageRequest.of(0, 10);



            Payment payment = new Payment();
            ReflectionTestUtils.setField(payment, "id", paymentId);

            List<Payment> payments = Arrays.asList(payment);

            Page<Payment> paymentPage = new PageImpl(payments);

            PaymentResponse response = new PaymentResponse(

                    UUID.randomUUID(),
                    "COMPLETED",
                    BigDecimal.valueOf(120.00),
                    PaymentMethod.PIX,
                    PaymentStatus.COMPLETED,
                    LocalDateTime.now(),
                    "Não falhou",
                    orderId,
                    "referencia do gateway",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            when(paymentMapper.toPaymentResponse(payment)).thenReturn(response);
            when(paymentRepository.findByUserId(userId, pageable)).thenReturn(paymentPage);

            Page<PaymentResponse>  output = paymentService.getPaymentByUserId(userId, pageable);

            assertNotNull(output);
            assertFalse(output.isEmpty());
            assertEquals(1, output.getTotalElements());
            assertEquals(response, output.getContent().get(0));

            verify(paymentRepository).findByUserId(userId, pageable);
            verify(paymentMapper).toPaymentResponse(payment);


        }
    }

    @Nested
    @DisplayName("Should cancel payment with sucesso")
    class ShouldCancelPaymentWithSucesso {

        @Test
        void shouldCancelPaymentWithSucesso() {

            UUID paymentId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            UUID orderId = UUID.randomUUID();

            User user = new User();
            ReflectionTestUtils.setField(user, "id", userId);

            Order order = new Order();
            ReflectionTestUtils.setField(order, "id", orderId);
            order.setUser(user);

            Payment payment = new Payment();
            ReflectionTestUtils.setField(payment, "id", paymentId);
            payment.setOrder(order);

            when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

            paymentService.cancelPayment(paymentId, userId);

            assertEquals(PaymentStatus.CANCELLED, payment.getStatus());
            assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());

            verify(paymentRepository).findById(paymentId);
            verify(paymentRepository).save(payment);



        }
    }


}
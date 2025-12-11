package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.order.Order;
import com.gustavosdaniel.ecommerce_api.order.OrderMapper;
import com.gustavosdaniel.ecommerce_api.order.OrderNotFoundException;
import com.gustavosdaniel.ecommerce_api.order.OrderRepository;
import com.gustavosdaniel.ecommerce_api.user.UserNotAuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentGatewaySimulateService paymentGatewaySimulateService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderRepository orderRepository;
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final OrderMapper orderMapper;

    public PaymentServiceImpl(PaymentGatewaySimulateService paymentGatewaySimulateService, PaymentRepository paymentRepository, PaymentMapper paymentMapper, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.paymentGatewaySimulateService = paymentGatewaySimulateService;
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }


    @Override
    public Payment processPayment(Order order, PaymentRequest paymentRequest) {


        if (paymentRequest.amount().compareTo(order.getTotalAmount()) < 0){

            throw new PaymentValueInsuficienteException();
        }

        Payment payment = paymentMapper.toPayment(paymentRequest);
        payment.setOrder(order);
        payment.processPayment();

        Payment savedPayment = paymentRepository.save(payment);

        boolean approved = paymentGatewaySimulateService.ProcessPayment(paymentRequest);

        if(approved){

            savedPayment.completePayment();

            log.info("Pagamento aprovado para o pedido {}", order.getId());

        }else {

            savedPayment.failPayment("Transiçao recusada pela operadora");

            log.warn("Pagamento recusado para o pedido {}", order.getId());        }

        return paymentRepository.save(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentResponse> getPaymentById(UUID id) {

        log.info("Buscando pagamento pelo id {}", id);

        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isEmpty()) {

            log.info("Nenhum pagamento com esse id {} foi encontrado", id);

            return Optional.empty();
        }

        log.info("Retornando pagamento encontrado pelo id {}", id);

        return payment.map(paymentMapper::toPaymentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentResponse> getPaymentByOrderId(UUID orderId, UUID userId) {

        log.info("Buscando pagamento do pedido {} para usuário {}", orderId, userId);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (!order.getUser().getId().equals(userId)) {

            throw new UserNotAuthorizationException();
        }

        Optional<Payment> payment = paymentRepository.findByOrder_IdAndOrder_User_Id(orderId, userId);

        log.info("Payment encontrado para a order {}", orderId);

        return payment.map(paymentMapper::toPaymentResponse);
    }
}

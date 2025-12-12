package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.order.Order;
import com.gustavosdaniel.ecommerce_api.order.OrderMapper;
import com.gustavosdaniel.ecommerce_api.order.OrderNotFoundException;
import com.gustavosdaniel.ecommerce_api.order.OrderRepository;
import com.gustavosdaniel.ecommerce_api.user.User;
import com.gustavosdaniel.ecommerce_api.user.UserNotAuthorizationException;
import com.gustavosdaniel.ecommerce_api.user.UserNotFoundException;
import com.gustavosdaniel.ecommerce_api.user.UserRepository;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final StripePaymentGatewayService stripePaymentGatewayService;
    private final PaymentGatewaySimulateService paymentGatewaySimulateService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderRepository orderRepository;
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    public PaymentServiceImpl(StripePaymentGatewayService stripePaymentGatewayService, PaymentGatewaySimulateService paymentGatewaySimulateService, PaymentRepository paymentRepository, PaymentMapper paymentMapper, OrderRepository orderRepository) {
        this.stripePaymentGatewayService = stripePaymentGatewayService;
        this.paymentGatewaySimulateService = paymentGatewaySimulateService;
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.orderRepository = orderRepository;
    }


    @Override
    public Payment processPayment(Order order, PaymentRequest paymentRequest) {

        if (paymentRequest.amount().compareTo(order.getTotalAmount()) < 0){

            throw new PaymentValueInsuficienteException();
        }

        Payment payment = paymentMapper.toPayment(paymentRequest);
        payment.setOrder(order);
        payment.processPayment();

        PaymentIntent paymentIntent = stripePaymentGatewayService.createPaymentIntent(order);

        payment.setReference(paymentIntent.getId());

        log.info("Intenção de pagamento criada no Stripe: {}", payment.getReference());

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentByUserId(UUID userId, Pageable pageable) {

        log.info("Buscando lista de pagamento do user {}", userId);

        Page<Payment> payments = paymentRepository.findByUserId(userId, pageable);

        if(payments.isEmpty()){

            log.info("Nenhum pagamento foi encontrado para esse usuario");

            return Page.empty();
        }

        log.info("Retornado lista de pagamentos do user {}", payments.getNumberOfElements());

        return payments.map(paymentMapper::toPaymentResponse);
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

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse refundPayment(UUID paymentId, UUID userId) {

        log.info("Iniciando processo de reembolso do pagamento {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(PaymentNotFoundException::new);

        if (!payment.getOrder().getUser().getId().equals(userId)) {

            throw new UserNotAuthorizationException();
        }

        if (!payment.getStatus().equals(PaymentStatus.COMPLETED)) {

            throw new PaymentStatusCompleteException();
        }

        try {

            stripePaymentGatewayService.refundPayment(payment.getReference());

        }catch (Exception e){

            log.error("Erro ao processar estorno no Stripe", e);


            throw new RuntimeException("Erro no Gateway de Pagamento: " + e.getMessage());
        }

        payment.refundPayment();

        paymentRepository.save(payment);

        log.info("Proesso de reembolso finalizado com sucesso");

        return paymentMapper.toPaymentResponse(payment);
    }

    @Override
    @jakarta.transaction.Transactional
    public void cancelPayment(UUID paymentId, UUID userId) {

        log.info("Cancelando pagamento {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(PaymentNotFoundException::new);

        if (!payment.getOrder().getUser().getId().equals(userId)) {

            throw new UserNotAuthorizationException();
        }

        if (payment.getReference() != null) {

            try {
                stripePaymentGatewayService.cancelPaymentIntent(payment.getReference());
            } catch (Exception e){

                log.warn("Erro ao cancelar no Stripe (pode já ter sido cancelado): {}", e.getMessage());

                throw new RuntimeException("Erro ao cancelar transação no gateway.");

            }
        }

        payment.cancelPayment();

        Order order = payment.getOrder();

        order.cancelOrder();

        paymentRepository.save(payment);

        log.info("Pagamento cancelado com sucesso {}", paymentId);
    }

    @Override
    public void handleGatewayNotification(String payload, String signature) {

        if (stripeWebhookSecret == null || stripeWebhookSecret.isEmpty()) {

            throw new RuntimeException("Stripe Webhook Secret não configurado!");
        }

        Event event;

        try {
            event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);
        } catch (Exception e) {

            log.error("Erro ao validar assinatura do Webhook Stripe", e);

            throw new RuntimeException("Assinatura inválida");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {

            log.info("Evento de sucesso recebido do Stripe: {}", event.getId());

            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            if (stripeObject instanceof PaymentIntent paymentIntent) {

                String stripeId = paymentIntent.getId();

                Payment payment = paymentRepository.findByReference(stripeId).orElseThrow(PaymentNotFoundException::new);

                if (payment.getStatus() == PaymentStatus.COMPLETED) {

                    log.info("Pagamento {} já foi processado anteriormente.", payment.getId());
                    return;
                }

                payment.completePayment();
                paymentRepository.save(payment);

                log.info("Pagamento confirmado com sucesso! Order ID: {}", payment.getOrder().getId());

            }else {
                log.warn("Objeto do evento não é um PaymentIntent valido.");
            }
        }else {
            log.debug("Evento ignorado: {}", event.getType());
        }

    }

    @Value("${stripe.webhook-secret}")
    private String stripeWebhookSecret;
}

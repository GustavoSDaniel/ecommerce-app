package com.gustavosdaniel.ecommerce_api.payment;

import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PaymentMapper {

    public Payment toPayment(PaymentRequest paymentRequest) {

        if (paymentRequest == null) {
            return null;
        }

        Payment payment = new Payment();

        payment.setAmount(paymentRequest.amount());
        payment.setReference(UUID.randomUUID().toString());

        PaymentDetails paymentDetails = paymentRequest.details();

        if (paymentDetails instanceof CreditCardDetails){

            payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        }

        if (paymentDetails instanceof DebitCardDetails){

            payment.setPaymentMethod(PaymentMethod.DEBIT_CARD);

        }

        if (paymentDetails instanceof PixDetails){

            payment.setPaymentMethod(PaymentMethod.PIX);
        }

        if (paymentDetails instanceof BoletoDetails){

            payment.setPaymentMethod(PaymentMethod.BOLETO);
        } else {
            throw new IllegalArgumentException("Metodo de pagamento invalido");
        }

        return payment;
    }

    public PaymentResponse toPaymentResponse(Payment payment) {

        if (payment == null) {
            return null;
        }

        return new PaymentResponse(

                payment.getId(),
                payment.getReference(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getConfirmedAt(),
                payment.getFailureReason(),
                payment.getOrder() != null ? payment.getOrder().getId() : null,
                payment.getReference(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()

        );

    }
}

package com.gustavosdaniel.ecommerce_api.payment;

import org.springframework.stereotype.Component;



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


        PaymentMethod method = switch (paymentRequest.details()) {

            case CreditCardDetails c -> PaymentMethod.CREDIT_CARD;

            case DebitCardDetails d -> PaymentMethod.DEBIT_CARD;

            case PixDetails p        -> PaymentMethod.PIX;

            case BoletoDetails b     -> PaymentMethod.BOLETO;

            default -> throw new IllegalArgumentException("Metodo de pagamento invalido");
        };

        payment.setPaymentMethod(method);

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

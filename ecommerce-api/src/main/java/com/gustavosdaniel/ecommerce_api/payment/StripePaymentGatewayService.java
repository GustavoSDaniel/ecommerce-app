package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.order.Order;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripePaymentGatewayService {

    private static final Logger log = LoggerFactory.getLogger(StripePaymentGatewayService.class);

    public PaymentIntent createPaymentIntent(Order order) {


        try {

            Long amountInCents = order.getTotalAmount().multiply(new BigDecimal(100)).longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("brl")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                    )
                    .putMetadata("order_id", order.getId().toString())
                    .putMetadata("user_id", order.getUser().getId().toString())
                    .build();

            return PaymentIntent.create(params);
        }catch (StripeException exception){

            throw new RuntimeException("Erro ao comunicar com o Stripe: " + exception.getMessage());
        }
    }

    public void cancelPaymentIntent(String paymentIntentId) {

        try {
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);

            intent.cancel();

        }catch (StripeException e){

            throw new RuntimeException("Não foi possível cancelar no Stripe: " + e.getMessage());
        }

    }
    public void refundPayment(String paymentIntentId){

        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .build();

            Refund.create(params);
        } catch (StripeException exception){

            throw new RuntimeException("Erro ao realizar estorno no Stripe: " + exception.getMessage());
        }
    }
}

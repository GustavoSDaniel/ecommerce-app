package com.gustavosdaniel.ecommerce_api.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewaySimulateService {

    Logger log = LoggerFactory.getLogger(PaymentGatewaySimulateService.class);

    public boolean ProcessPayment(PaymentRequest paymentRequest) {

        log.info("Simulando processo de pagamento... RS {}", paymentRequest.amount());

        try {

            Thread.sleep(1000);

        }catch(InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        boolean aprproved = Math.random() < 0.9;

        if(aprproved) {

            log.info("Simulando processo de pagamento... APRPROVED");

        }else  {

            log.info("Simulando processo de pagamento... REPROVED");
        }
        return aprproved;
    }
}

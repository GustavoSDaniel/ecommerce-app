package com.gustavosdaniel.ecommerce_api.payment;


public record PixDetails(

        String payerName,
        String payerDocument

) implements PaymentDetails {}

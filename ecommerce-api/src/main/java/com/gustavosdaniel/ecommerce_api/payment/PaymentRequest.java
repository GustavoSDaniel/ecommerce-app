package com.gustavosdaniel.ecommerce_api.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull
        @Positive(message = "O valor do pagamento deve ser positivo")
        BigDecimal amount,

        @NotNull(message = "Os detalhes do pagamento são obrigatórios")
        @Valid
        PaymentDetails details

        ) {
}

package com.gustavosdaniel.ecommerce_api.payment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record BoletoDetails(

        @NotBlank(message = "O documento do pagador é obrigatório")
        @CPF(message = "CPF inválido")
        @CNPJ(message = "CNPJ inválido")
        String payerDocument,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 200, message = "O endereço excede o limite de caracteres permitido")
        String address,

        @NotNull(message = "A data de vencimento é obrigatória")
        @FutureOrPresent(message = "A data de vencimento deve ser hoje ou uma data futura")
        LocalDate dueDate

) implements PaymentDetails { }

package com.gustavosdaniel.ecommerce_api.payment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.CreditCardNumber;

public record CreditCardDetails(

        @NotBlank(message = "O número do cartão é obrigatório")
        @CreditCardNumber(ignoreNonDigitCharacters = true, message = "Número de cartão inválido (Falha no algoritmo de Luhn)")
        String cardNumber,

        @NotBlank(message = "O nome do titular é obrigatório")
        @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "O nome deve conter apenas letras")
        String cardHolderName,

        @NotBlank(message = "A data de validade é obrigatória")
        @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "A data deve estar no formato MM/YY")
        String expiryDate,

        @NotBlank(message = "O CVV é obrigatório")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "O CVV deve ter 3 ou 4 dígitos")
        String cvv

) implements PaymentDetails { }

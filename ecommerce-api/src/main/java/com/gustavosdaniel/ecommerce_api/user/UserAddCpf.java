package com.gustavosdaniel.ecommerce_api.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAddCpf(

        @NotBlank(message = "O campo CPF não pode esta vazio ele é obrigaótio")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas os 11 números")
        String cpf
) {
}

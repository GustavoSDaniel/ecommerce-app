package com.gustavosdaniel.ecommerce_api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Para realizar o lgin o email é obrigatório")
        @Email(message = "Formato de email é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatório")
        String password

) {
}

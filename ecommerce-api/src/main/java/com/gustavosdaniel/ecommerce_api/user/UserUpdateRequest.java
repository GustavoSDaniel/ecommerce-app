package com.gustavosdaniel.ecommerce_api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        @Size(max = 70, message = "A quantidade de caracteres é no maximo de 70")
        @Pattern(regexp = "^[a-zA-Z0-9]+$",
                message = "O nome de usuário deve conter apenas letras e números")
        String userName,

        String password,

        @Email(message = "Formato do emaill é obrigatório")
        @Size(max = 100, message = "A quantidade de caracteres é no maximo de 100")
        String email,

        UserRole role,

        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter DDD + Número (apenas dígitos)")
        String phoneNumber

){
}

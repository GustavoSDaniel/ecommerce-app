package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.address.AddressRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        @Size(max = 70, message = "A quantidade de caracteres é no maximo de 70")
        @Pattern(regexp = "^[a-zA-Z0-9]+$",
                message = "O nome de usuário deve conter apenas letras e números")
        String userName,

        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String password,

        @Email(message = "Formato do emaill é obrigatório")
        @Size(max = 100, message = "A quantidade de caracteres é no maximo de 100")
        String email,

        UserRole role,

        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter DDD + Número (apenas dígitos)")
        String phoneNumber,

        @Valid
        AddressRequest address

){
}

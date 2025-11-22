package com.gustavosdaniel.ecommerce_api.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AddressRequest(

        @NotNull(message = "O ID do usuário é obrigatório")
        UUID userId,

        @NotBlank(message = "O número da casa é obrigatório")
        @Size(max = 10, message = "O número da casa deve ter no máximo 10 caracteres")
        String houseNumber,

        @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres")
        String complement,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "^\\d{5}-\\d{3}$|^\\d{8}$",
                message = "O CEP deve estar no formato 99999-999 ou 99999999.")
        String zipCode,

        @NotBlank(message = "O nome da rua é obrigatório")
        @Size(max = 100, message = "O nome da rua deve ter no máximo 100 caracteres")
        String street,

        @NotBlank(message = "O nome do bairro é obrigatório")
        @Size(max = 100, message = "O nome do bairro deve ter no máximo 100 caracteres")
        String bairro,

        @NotBlank(message = "O nome do cidade é obrigatório")
        @Size(max = 100, message = "O nome da cidade deve ter no máximo 100 caracteres")
        String city,

        @NotBlank(message = "O nome do estado é obrigatório")
        @Size(max = 50, message = "O nome do estado deve ter no máximo 50 caracteres")
        String state,

        @NotBlank(message = "O pais é obrigatório")
        @Size(max = 50, message = "O país deve ter no máximo 50 caracteres")
        String country


) {
}

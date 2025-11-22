package com.gustavosdaniel.ecommerce_api.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AddressResponse(

        Long id,

        String userName,

        String houseNumber,

        String complement,

        String zipCode,

        String street,

        String bairro,

        String city,

        String state,

        String country

) {
}

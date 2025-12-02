package com.gustavosdaniel.ecommerce_api.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Nome da categoria é obrigatório")
        @Size(min = 4, message = "O nome da categoria tem que te no minimo 4 digitos")
        String name,

        @NotBlank(message = "A descrição da categoria é obrigatório")
        @Size(min = 4, message = "A descrição da categoria tem que te no minimo 4 digitos")
        String description

) {
}

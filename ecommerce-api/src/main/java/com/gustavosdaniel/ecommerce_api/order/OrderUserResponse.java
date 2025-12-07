package com.gustavosdaniel.ecommerce_api.order;

import java.util.UUID;

public record OrderUserResponse(

        UUID userId,
        String name,
        String email
) {
}

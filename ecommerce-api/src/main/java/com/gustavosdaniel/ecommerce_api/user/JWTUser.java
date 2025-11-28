package com.gustavosdaniel.ecommerce_api.user;

import java.util.UUID;

public record JWTUser(

        UUID UserId,
        String email,
        String role
) {
}

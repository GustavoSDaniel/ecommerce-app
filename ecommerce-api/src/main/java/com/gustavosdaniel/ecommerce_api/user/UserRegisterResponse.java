package com.gustavosdaniel.ecommerce_api.user;

import java.util.UUID;

public record UserRegisterResponse(

        UUID userId,

        String userName,

        String email,

        UserRole role
) {
}

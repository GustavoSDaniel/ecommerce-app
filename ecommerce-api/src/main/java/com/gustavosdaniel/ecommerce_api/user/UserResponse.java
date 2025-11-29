package com.gustavosdaniel.ecommerce_api.user;

import java.util.UUID;

public record UserResponse(

        UUID id,
        String userName,
        String email,
        UserRole role

) {
}

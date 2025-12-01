package com.gustavosdaniel.ecommerce_api.user;

import java.util.UUID;

public record UseUpdateResponse(

        UUID id,
        String userName,
        String email,
        UserRole role,
        String phoneNumber
) {
}

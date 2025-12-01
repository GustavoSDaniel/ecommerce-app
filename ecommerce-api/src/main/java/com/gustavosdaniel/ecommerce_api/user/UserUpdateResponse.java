package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.address.AddressResponse;

import java.util.List;
import java.util.UUID;

public record UserUpdateResponse(

        UUID id,
        String userName,
        String email,
        UserRole role,
        String phoneNumber,
        List<AddressResponse> address
) {
}

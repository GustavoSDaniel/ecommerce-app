package com.gustavosdaniel.ecommerce_api.address;


import java.util.UUID;

public interface AddressService {

    AddressResponse createAddress(UUID userId, AddressRequest address);
}

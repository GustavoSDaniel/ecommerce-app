package com.gustavosdaniel.ecommerce_api.address;

import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toAddress(AddressRequest addressRequest) {
        if(addressRequest == null) return null;

        return new Address(

                addressRequest.houseNumber(),
                addressRequest.complement(),
                addressRequest.zipCode(),
                addressRequest.street(),
                addressRequest.bairro(),
                addressRequest.city(),
                addressRequest.state(),
                addressRequest.country()

        );
    }

    public AddressResponse toAddressResponse(Address address) {
        if(address == null) return null;

        return new AddressResponse(
                address.getId(),
                address.getUser() != null ? address.getUser().getUserName() : null,
                address.getHouseNumber(),
                address.getComplement(),
                address.getZipCode(),
                address.getStreet(),
                address.getBairro(),
                address.getCity(),
                address.getState(),
                address.getCountry()

        );
    }
}

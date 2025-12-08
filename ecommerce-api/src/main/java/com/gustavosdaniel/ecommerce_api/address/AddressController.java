package com.gustavosdaniel.ecommerce_api.address;

import com.gustavosdaniel.ecommerce_api.config.UserAuthorizationRole;
import com.gustavosdaniel.ecommerce_api.user.JWTUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;

    }

    @PostMapping
    @Operation(summary = "Adiciona endereço para o usuario")
    public ResponseEntity<AddressResponse> addAddress(
            Authentication authentication,
            @RequestBody @Valid AddressRequest addressRequest) {

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        AddressResponse address = addressService.createAddress(userId, addressRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(address.id())
                .toUri();

        return ResponseEntity.created(location).body(address);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove endereço do usuario")
    public ResponseEntity<Void> removeAddress(Authentication authentication, @PathVariable("id") Long id) {

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        addressService.deleteAddress(userId, id);

        return ResponseEntity.noContent().build();

    }
}

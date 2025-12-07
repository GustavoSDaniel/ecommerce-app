package com.gustavosdaniel.ecommerce_api.order;

import com.auth0.jwt.JWT;
import com.gustavosdaniel.ecommerce_api.config.UserAuthorizationRole;
import com.gustavosdaniel.ecommerce_api.product.InsuficienteStockException;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionAddAndRemove;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionSet;
import com.gustavosdaniel.ecommerce_api.user.JWTUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserAuthorizationRole userAuthorizationRole;


    public OrderController(OrderService orderService, UserAuthorizationRole userAuthorizationRole) {
        this.orderService = orderService;
        this.userAuthorizationRole = userAuthorizationRole;
    }

    @PostMapping
    @Operation(summary = "Cria pedido")
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication,
            @RequestBody @Valid OrderRequest orderRequest)
            throws StockOperationExceptionAddAndRemove, StockOperationExceptionSet, InsuficienteStockException {

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        userAuthorizationRole.validateUserRole(userId, authentication);

        OrderResponse order = orderService.createOrder(userId, orderRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.id())
                .toUri();

        return ResponseEntity.created(location).body(order);

    }

    @GetMapping("/orderId/{id}")
    @Operation(summary = "Busca order pelo ID")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id, Authentication authentication) {

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        userAuthorizationRole.validateUserRole(userId, authentication);

        OrderResponse order = orderService.getOrderById(id);

        return ResponseEntity.ok(order);

    }


}

package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.config.UserAuthorizationRole;
import com.gustavosdaniel.ecommerce_api.payment.PaymentRequest;
import com.gustavosdaniel.ecommerce_api.payment.PaymentResponse;
import com.gustavosdaniel.ecommerce_api.product.InsuficienteStockException;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionAddAndRemove;
import com.gustavosdaniel.ecommerce_api.product.StockOperationExceptionSet;
import com.gustavosdaniel.ecommerce_api.user.JWTUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
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

    @GetMapping
    @Operation(summary = "Busca todos os pedidos")
    public ResponseEntity<Page<OrderResponse>> findAll(

            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){

        Page<OrderResponse> orders = orderService.getAllOrders(pageable);

        return ResponseEntity.ok(orders);

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

    @GetMapping("/my-orders")
    @Operation(summary = "Retorna os pedidos do usuario")
    public ResponseEntity<Page<OrderResponse>> getOrdersByUserId(
            Authentication authentication,
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable){

                JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

                UUID userId = jwtUser.UserId();

                Page<OrderResponse> orders = orderService.getOrdersByUserId(userId, pageable);

                return ResponseEntity.ok(orders);
    }

    @GetMapping("/by-status")
    @Operation(summary = "Busca pedidos por status")
    public ResponseEntity<Page<OrderResponse>> getOrdersByStatus(
            @RequestParam OrderStatus status,
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){

        Page<OrderResponse> orders = orderService.getOrderByStatus(status, pageable);

        return ResponseEntity.ok(orders);

    }

    @GetMapping("my-orders-status")
    @Operation(summary = "Busca pedidos do usuario pelo status")
    public ResponseEntity<Page<OrderResponse>> getOrdersByUserByStatus(

            Authentication authentication,
            @RequestParam(required = false) OrderStatus status,
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        Page<OrderResponse> orders = orderService.getOrdersByUserAndStatus(userId, status, pageable);

        return ResponseEntity.ok(orders);

    }

    @GetMapping("/my-reference")
    @Operation(summary = "Busca pedido pela referencia")
    public ResponseEntity<OrderResponse> getOrderByReference(
            Authentication authentication,
            @RequestParam String reference
    ){

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        Optional<OrderResponse> order = orderService.searchOrdersByReference(userId, reference);

        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/confirmPayment")
    @Operation(summary = "Marca pedido com pagamento confirmado")
    public ResponseEntity<PaymentResponse> confirmOrderPayment(
            @PathVariable("id") UUID id, @RequestBody @Valid  PaymentRequest paymentRequest) {

        PaymentResponse paymentResponse = orderService.confirmPayment(id, paymentRequest);

        return ResponseEntity.ok(paymentResponse);
    }

    @PatchMapping("/{id}/ship")
    @Operation(summary = "Marca pedido como enviado")
    public ResponseEntity<Void> shippedOrder(@PathVariable("id") UUID id) {

        orderService.shippedOrder(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deliver")
    @Operation(summary = "Marca pedido como entregue")
    public ResponseEntity<Void> deliveredOrder(@PathVariable("id") UUID id) {

        orderService.deliveredOrder(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("canceled/{id}")
    @Operation(summary = "Cancela Pedido")
    public ResponseEntity<Void> cancelOrder(@PathVariable("id") UUID id, Authentication authentication)
            throws StockOperationExceptionAddAndRemove, StockOperationExceptionSet, InsuficienteStockException {

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        orderService.cancelOrder(id, userId);

        return ResponseEntity.ok().build();
    }

}

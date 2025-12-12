package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.config.UserAuthorizationRole;
import com.gustavosdaniel.ecommerce_api.user.JWTUser;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserAuthorizationRole userAuthorizationRole;

    public PaymentController(PaymentService paymentService, UserAuthorizationRole userAuthorizationRole) {
        this.paymentService = paymentService;
        this.userAuthorizationRole = userAuthorizationRole;
    }

    @GetMapping("by-id")
    @Operation(summary = "Busca o pagamento pelo ID")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable("id") UUID id) {

        Optional<PaymentResponse> payment = paymentService.getPaymentById(id);

        return payment.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Busca pagamento do usuario")
    public ResponseEntity<Page<PaymentResponse>> getPaymentByUser(

            @PathVariable("userId")
            UUID userId,
            Authentication authentication,
            @ParameterObject()
            @PageableDefault(sort = "confirmedAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        userAuthorizationRole.validateUserRole(userId, authentication);

        Page<PaymentResponse> payments = paymentService.getPaymentByUserId(userId, pageable);

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/by-order/{orderId}")
    @Operation(summary = "Busca pagamento através do ID do pedido")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable("orderId") UUID orderId,
            Authentication authentication) {

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        userAuthorizationRole.validateUserRole(userId, authentication);

        Optional<PaymentResponse>  payment = paymentService.getPaymentByOrderId(orderId, userId);

        return payment.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Faz o estorno do pagamento")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable("id") UUID id, Authentication authentication) {

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        PaymentResponse paymentRefund = paymentService.refundPayment(id, userId);

        return ResponseEntity.ok(paymentRefund);

    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancelando o pagamento")
    public ResponseEntity<Void> cancelPayment(@PathVariable("id") UUID id, Authentication authentication) {

        JWTUser jwtUser = (JWTUser) authentication.getPrincipal();

        UUID userId = jwtUser.UserId();

        paymentService.cancelPayment(id, userId);

        return ResponseEntity.noContent().build();
    }
}

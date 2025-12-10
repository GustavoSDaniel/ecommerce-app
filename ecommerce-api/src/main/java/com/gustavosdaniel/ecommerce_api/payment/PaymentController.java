package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.config.UserAuthorizationRole;
import com.gustavosdaniel.ecommerce_api.user.JWTUser;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

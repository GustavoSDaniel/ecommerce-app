package com.seuecommerce.util;

import com.gustavosdaniel.ecommerce_api.order.Order;
import com.gustavosdaniel.ecommerce_api.payment.Payment;
import com.gustavosdaniel.ecommerce_api.user.User;


public class EmailTemplates {

    public static String buildWelcomeTemplate(User user) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2 style="color: #2c3e50;">Bem-vindo, %s!</h2>
                <p>Seu cadastro foi realizado com sucesso.</p>
                <p>Aproveite nossas ofertas!</p>
            </body>
            </html>
            """.formatted(user.getUserName());
    }

    public static String buildOrderCreatedTemplate(Order order) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2 style="color: #2980b9;">Pedido #%s Recebido!</h2>
                <p>Valor Total: <strong>R$ %.2f</strong></p>
                <p>Aguardando confirmação do pagamento.</p>
            </body>
            </html>
            """.formatted(order.getId(), order.getTotalAmount());
    }

    public static String buildPaymentConfirmedTemplate(Payment payment) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2 style="color: #27ae60;">Pagamento Aprovado! </h2>
                <p>O pagamento do pedido <strong>#%s</strong> foi confirmado.</p>
                <p>Em breve enviaremos seus produtos.</p>
            </body>
            </html>
            """.formatted(payment.getOrder().getId());
    }
}
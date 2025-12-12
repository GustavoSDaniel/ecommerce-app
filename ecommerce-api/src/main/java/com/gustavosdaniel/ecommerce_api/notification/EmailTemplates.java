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

    public static String buildPaymentFailedTemplate(Payment payment, String failureReason) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif;">
            <h2 style="color: #c0392b;">Atenção: Falha no Pagamento</h2>
            
            <p>Lamentamos, mas o pagamento do seu pedido <strong>#%s</strong> não pôde ser processado.</p>
            
            <div style="border-left: 4px solid #c0392b; padding: 10px; margin: 15px 0; background-color: #fceae9;">
                <p><strong>Motivo da Falha:</strong> %s</p>
            </div>
            
            <p><strong>O que fazer agora?</strong></p>
            <ul>
                <li>Verifique se os dados do cartão/pagamento foram digitados corretamente.</li>
                <li>Tente novamente na área de pedidos, utilizando outra forma de pagamento.</li>
                <li>Se o problema persistir, entre em contato com seu banco ou com nosso suporte.</li>
            </ul>
            
            <p>Sua compra está aguardando um novo pagamento.</p>
        </body>
        </html>
        """.formatted(
                payment.getOrder().getId(),
                failureReason
        );
    }
}
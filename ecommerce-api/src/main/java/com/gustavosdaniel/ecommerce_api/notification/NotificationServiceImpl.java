package com.gustavosdaniel.ecommerce_api.notification;

import com.gustavosdaniel.ecommerce_api.order.Order;
import com.gustavosdaniel.ecommerce_api.payment.Payment;
import com.gustavosdaniel.ecommerce_api.user.User;


import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public NotificationServiceImpl(NotificationRepository notificationRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    @Override
    public void notifyUserRegistration(User user) {

        String html = com.seuecommerce.util.EmailTemplates.buildWelcomeTemplate(user);

        createAndSend(user, null, null, "Bem-Vindo ao Ecommerce", html);

    }

    @Override
    public void notifyOrderCreated(Order order) {

        String html = com.seuecommerce.util.EmailTemplates.buildOrderCreatedTemplate(order);

        createAndSend(order.getUser(), order, null, "Pedido recebido: #" + order.getId(), html);

    }

    @Override
    public void notifyPaymentConfirmed(Payment payment) {

        String html = com.seuecommerce.util.EmailTemplates.buildPaymentConfirmedTemplate(payment);

        createAndSend(
                payment.getOrder().getUser(),
                payment.getOrder(),
                payment,
                "Pagamento Aprovado  - Pedido #" + payment.getOrder().getId(),
                html);

    }

    @Override
    public void notifyPaymentFailed(Payment payment) {

        String failureReason = payment.getFailureReason();

        if (failureReason == null || failureReason.isBlank()) {
            failureReason = "Ocorreu um erro desconhecido no processamento do pagamento.";
        }


        String html = com.seuecommerce.util.EmailTemplates.buildPaymentFailedTemplate(payment, failureReason);

        createAndSend(
                payment.getOrder().getUser(),
                payment.getOrder(),
                payment,
                "Pagamento Recusado - Pedido #" + payment.getOrder().getId(),
                html);
    }

    private void createAndSend(User user, Order order, Payment payment, String subject, String content) {

        Notification notification = new Notification();
        notification.setRecipient(user.getEmail());
        notification.setSubject(subject);
        notification.setContent(content);
        notification.setUser(user);

        if (order != null) notification.setOrder(order);
        if (payment != null) notification.setPayment(payment);

        Notification savedNotification = notificationRepository.save(notification);

        emailService.sendEmail(savedNotification);
    }
}

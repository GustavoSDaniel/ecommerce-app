package com.gustavosdaniel.ecommerce_api.notification;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender, NotificationRepository notificationRepository) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Async
    public void sendEmail(Notification notification ){

        log.debug("Enviadon email para: {}", notification.getRecipient());

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(notification.getRecipient());
            helper.setSubject(notification.getSubject());
            helper.setText(notification.getContent(), true);

            mailSender.send(message);

            notification.markAsSent();
            notificationRepository.save(notification);

            log.info("Email enviado com sucesso para: {}", notification.getRecipient());

        }catch(Exception e){

            log.error(" Erro ao enviar email: {}", e.getMessage());

            notification.markAsFailed(e.getMessage());
            notificationRepository.save(notification);
        }

    }
}

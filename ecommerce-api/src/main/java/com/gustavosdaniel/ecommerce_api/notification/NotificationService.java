package com.gustavosdaniel.ecommerce_api.notification;

import com.gustavosdaniel.ecommerce_api.order.Order;
import com.gustavosdaniel.ecommerce_api.payment.Payment;
import com.gustavosdaniel.ecommerce_api.user.User;

public interface NotificationService {

    void notifyUserRegistration(User user);

    void notifyOrderCreated(Order order);

    void notifyPaymentConfirmed(Payment payment);
}

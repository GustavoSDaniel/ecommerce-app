package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.notification.Notification;
import com.gustavosdaniel.ecommerce_api.order.Order;
import com.gustavosdaniel.ecommerce_api.util.AuditableBase;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Payment extends AuditableBase {

    public Payment() {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String reference;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @OneToMany(mappedBy = "payment",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    @OneToOne(mappedBy = "payment", cascade = CascadeType.PERSIST)
    private Order order;

    public void processPayment() {

        if (this.status != PaymentStatus.PENDING) {
            throw new PaymentStatusProcessingException();
        }

        this.status = PaymentStatus.PROCESSING;
    }

    public void refundPayment() {

        if (this.status != PaymentStatus.COMPLETED) {

            throw new PaymentStatusRefundException();
        }

        this.status = PaymentStatus.REFUNDED;


    }

    public void completePayment() {

        if (this.status != PaymentStatus.PROCESSING) {
            throw new PaymentStatusCompleteException();
        }

        this.status = PaymentStatus.COMPLETED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void failPayment(String reason) {

        if (this.status != PaymentStatus.COMPLETED) {
            throw new PaymentStatusFailedException();
        }
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    public void cancelPayment() {

        if (this.status == PaymentStatus.COMPLETED || this.status == PaymentStatus.PROCESSING) {

            throw new PaymentStatusCancelledException();

        }
        this.status = PaymentStatus.CANCELLED;
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

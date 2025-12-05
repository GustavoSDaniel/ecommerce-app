package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.notification.Notification;
import com.gustavosdaniel.ecommerce_api.orderItem.OrderItem;
import com.gustavosdaniel.ecommerce_api.payment.Payment;
import com.gustavosdaniel.ecommerce_api.user.User;
import com.gustavosdaniel.ecommerce_api.util.AuditableBase;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order extends AuditableBase {

    public Order() {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @PrePersist
    public void prePersist() {

        if (this.reference == null) {
            this.reference = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }

    public void calculaTotal() {

        this.totalAmount = BigDecimal.ZERO;

        if (orderItems == null || orderItems.isEmpty()) {
            return;
        }
        for (OrderItem orderItem : orderItems) {

            if (orderItem.getUnitPrice() != null && orderItem.getQuantity() != null) {

                BigDecimal total = orderItem.getUnitPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

                this.totalAmount = this.totalAmount.add(total);

            }
        }

    }

    public void addOrderItem(OrderItem orderItem) {

        this.orderItems.add(orderItem);

        orderItem.setOrder(this);

        calculaTotal();
    }

    private void validateTransition(OrderStatus newStatus) {

        if (newStatus == null) {
            return;
        }

        switch (newStatus) {

            case PAID -> {

                if (this.orderStatus != OrderStatus.CREATED){

                    throw new OrderStatusPaidException();
                }

                if (this.totalAmount.compareTo(BigDecimal.ZERO) <= 0) {

                    throw new IllegalStateException("Não é possível pagar um pedido com valor zero");
                }
                if (this.orderItems.isEmpty()) {
                    throw new IllegalStateException("Não é possível pagar um pedido sem itens");
                }
            }

            case SHIPPED -> {

                if (this.orderStatus != OrderStatus.PAID) {

                    throw new OrderStatusShippedException();

                }
            }

            case DELIVERED -> {
                if (this.orderStatus != OrderStatus.SHIPPED) {

                    throw new OrderStatusDeliveredException();
                }
            }

            case CANCELLED -> {

                if (this.orderStatus == OrderStatus.SHIPPED || this.orderStatus == OrderStatus.DELIVERED) {

                    throw new OrderStatusCanceledException();
                }

            }

            default -> throw new IllegalStateException("Status inválido: " + newStatus);
        }
    }

    public void confirmPayment(Payment payment) {

        validateTransition(OrderStatus.PAID);

        if (payment == null) {

            throw new IllegalArgumentException("Não é possível confirmar o pedido sem um pagamento válido.");
        }

        this.payment = payment;
        this.payment.setOrder(this);


        this.orderStatus = OrderStatus.PAID;
    }

    public void shipOrder() {

        validateTransition(OrderStatus.SHIPPED);

        this.orderStatus = OrderStatus.SHIPPED;
    }

    public void deliverOrder() {
        validateTransition(OrderStatus.DELIVERED);
        this.orderStatus = OrderStatus.DELIVERED;
    }

    public void cancelOrder() {
        validateTransition(OrderStatus.CANCELLED);
        this.orderStatus = OrderStatus.CANCELLED;
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

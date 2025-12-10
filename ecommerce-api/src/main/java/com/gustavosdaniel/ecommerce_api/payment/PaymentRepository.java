package com.gustavosdaniel.ecommerce_api.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByOrder_IdAndOrder_User_Id(UUID orderId, UUID userId);}

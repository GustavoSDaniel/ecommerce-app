package com.gustavosdaniel.ecommerce_api.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p FROM Payment p WHERE p.order.user.id = :userId")
    Page<Payment> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    Optional<Payment> findByOrder_IdAndOrder_User_Id(UUID orderId, UUID userId);

    Optional<Payment> findByReference(String reference);
}


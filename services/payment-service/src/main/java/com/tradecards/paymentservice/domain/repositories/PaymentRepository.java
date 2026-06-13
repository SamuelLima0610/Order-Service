package com.tradecards.paymentservice.domain.repositories;

import com.tradecards.paymentservice.domain.entities.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByOrderId(Long orderId);
    List<Payment> findAll();
    boolean existsById(Long id);
}

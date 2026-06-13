package com.tradecards.paymentservice.infrastructure.persistence.repositories;

import com.tradecards.paymentservice.domain.entities.Payment;
import com.tradecards.paymentservice.domain.repositories.PaymentRepository;
import com.tradecards.paymentservice.infrastructure.persistence.mappers.PaymentEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentEntityMapper paymentEntityMapper;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository, PaymentEntityMapper paymentEntityMapper) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.paymentEntityMapper = paymentEntityMapper;
    }

    @Override
    public Payment save(Payment payment) {
        var entity = paymentEntityMapper.toEntity(payment);
        var savedEntity = paymentJpaRepository.save(entity);
        return paymentEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentJpaRepository.findById(id)
                .map(paymentEntityMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(paymentEntityMapper::toDomain);
    }

    @Override
    public List<Payment> findAll() {
        return paymentJpaRepository.findAll().stream()
                .map(paymentEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return paymentJpaRepository.existsById(id);
    }
}

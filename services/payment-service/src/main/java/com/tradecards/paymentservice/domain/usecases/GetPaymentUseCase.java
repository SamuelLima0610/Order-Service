package com.tradecards.paymentservice.domain.usecases;

import com.tradecards.paymentservice.domain.entities.Payment;
import com.tradecards.paymentservice.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetPaymentUseCase {
    private final PaymentRepository paymentRepository;

    public GetPaymentUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Optional<Payment> execute(Long id) {
        return paymentRepository.findById(id);
    }
}

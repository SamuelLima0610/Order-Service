package com.tradecards.paymentservice.domain.usecases;

import com.tradecards.paymentservice.domain.entities.Payment;
import com.tradecards.paymentservice.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Component;

@Component
public class CreatePaymentUseCase {
    private final PaymentRepository paymentRepository;

    public CreatePaymentUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment execute(Payment payment) {
        return paymentRepository.save(payment);
    }
}

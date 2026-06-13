package com.tradecards.paymentservice.domain.usecases;

import com.tradecards.paymentservice.domain.entities.Payment;
import com.tradecards.paymentservice.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListPaymentsUseCase {
    private final PaymentRepository paymentRepository;

    public ListPaymentsUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> execute() {
        return paymentRepository.findAll();
    }
}

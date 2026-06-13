package com.tradecards.paymentservice.application.services;

import com.tradecards.paymentservice.application.dtos.PaymentResponse;
import com.tradecards.paymentservice.application.mappers.PaymentMapper;
import com.tradecards.paymentservice.domain.entities.Payment;
import com.tradecards.paymentservice.domain.entities.PaymentStatus;
import com.tradecards.paymentservice.domain.usecases.CreatePaymentUseCase;
import com.tradecards.paymentservice.domain.usecases.GetPaymentUseCase;
import com.tradecards.paymentservice.domain.usecases.ListPaymentsUseCase;
import com.tradecards.shared.events.OrderCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final CreatePaymentUseCase createPaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    private final ListPaymentsUseCase listPaymentsUseCase;
    private final PaymentMapper paymentMapper;

    public PaymentService(CreatePaymentUseCase createPaymentUseCase,
                          GetPaymentUseCase getPaymentUseCase,
                          ListPaymentsUseCase listPaymentsUseCase,
                          PaymentMapper paymentMapper) {
        this.createPaymentUseCase = createPaymentUseCase;
        this.getPaymentUseCase = getPaymentUseCase;
        this.listPaymentsUseCase = listPaymentsUseCase;
        this.paymentMapper = paymentMapper;
    }

    public PaymentResponse createPaymentFromOrder(OrderCreatedEvent event) {
        Payment payment = new Payment();
        payment.setOrderId(event.getId());
        payment.setUserId(event.getUserId());
        payment.setAmount(event.getAmount());
        payment.setStatus(PaymentStatus.PENDING);

        Payment savedPayment = createPaymentUseCase.execute(payment);
        return paymentMapper.toResponse(savedPayment);
    }

    public Optional<PaymentResponse> getPaymentById(Long id) {
        return getPaymentUseCase.execute(id)
                .map(paymentMapper::toResponse);
    }

    public List<PaymentResponse> getAllPayments() {
        return listPaymentsUseCase.execute().stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }
}

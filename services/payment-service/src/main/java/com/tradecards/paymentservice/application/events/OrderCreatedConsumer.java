package com.tradecards.paymentservice.application.events;

import com.tradecards.paymentservice.application.dtos.PaymentResponse;
import com.tradecards.paymentservice.application.mappers.PaymentMapper;
import com.tradecards.paymentservice.application.services.PaymentService;
import com.tradecards.shared.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    public OrderCreatedConsumer(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @KafkaListener(topics = "order.created", groupId = "payment-service")
    public void consume(OrderCreatedEvent event) {
        log.info("Received order.created event for orderId={}, userId={}, amount={}",
                event.getId(), event.getUserId(), event.getAmount());

        PaymentResponse payment = paymentService.createPaymentFromOrder(event);

        log.info("Payment created with id={} for orderId={}", payment.getId(), payment.getOrderId());
    }
}

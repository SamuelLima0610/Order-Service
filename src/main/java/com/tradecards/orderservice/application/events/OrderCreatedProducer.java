package com.tradecards.orderservice.application.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "order.created";

    public OrderCreatedProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(OrderCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
    }
}

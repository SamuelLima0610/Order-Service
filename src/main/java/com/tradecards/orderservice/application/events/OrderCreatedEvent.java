package com.tradecards.orderservice.application.events;

import java.math.BigDecimal;

public class OrderCreatedEvent {
    private Long id;
    private Long userId;
    private BigDecimal amount;

    public OrderCreatedEvent(Long id, Long userId, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}

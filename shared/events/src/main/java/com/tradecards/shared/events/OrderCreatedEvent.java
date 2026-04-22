package com.tradecards.shared.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Event published when a new order is created.
 * This event is shared across microservices via Kafka topic: order.created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("amount")
    private BigDecimal amount;
    
    @JsonProperty("timestamp")
    private Instant timestamp;
    
    @JsonProperty("eventId")
    private String eventId;
    
    /**
     * Constructor for backward compatibility
     */
    public OrderCreatedEvent(Long id, Long userId, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.timestamp = Instant.now();
        this.eventId = java.util.UUID.randomUUID().toString();
    }
}

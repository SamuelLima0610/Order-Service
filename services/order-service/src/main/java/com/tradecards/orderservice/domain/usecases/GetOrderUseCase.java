package com.tradecards.orderservice.domain.usecases;

import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.domain.repositories.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetOrderUseCase {
    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> execute(Long id) {
        return orderRepository.findById(id);
    }
}

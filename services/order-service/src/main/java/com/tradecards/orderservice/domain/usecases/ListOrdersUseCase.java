package com.tradecards.orderservice.domain.usecases;

import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.domain.repositories.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListOrdersUseCase {
    private final OrderRepository orderRepository;

    public ListOrdersUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> execute() {
        return orderRepository.findAll();
    }
}

package com.tradecards.orderservice.domain.usecases;

import com.tradecards.orderservice.domain.repositories.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteOrderUseCase {
    private final OrderRepository orderRepository;

    public DeleteOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void execute(Long id) {
        orderRepository.deleteById(id);
    }
}

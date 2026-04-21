package com.tradecards.orderservice.application.services;

import com.tradecards.orderservice.application.dtos.OrderRequest;
import com.tradecards.orderservice.application.dtos.OrderResponse;
import com.tradecards.orderservice.application.mappers.OrderMapper;
import com.tradecards.orderservice.application.events.OrderCreatedProducer;
import com.tradecards.orderservice.application.events.OrderCreatedEvent;
import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.domain.usecases.CreateOrderUseCase;
import com.tradecards.orderservice.domain.usecases.DeleteOrderUseCase;
import com.tradecards.orderservice.domain.usecases.GetOrderUseCase;
import com.tradecards.orderservice.domain.usecases.ListOrdersUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final OrderMapper orderMapper;
    private final OrderCreatedProducer orderCreatedProducer;

    public OrderService(CreateOrderUseCase createOrderUseCase,
                        GetOrderUseCase getOrderUseCase,
                        ListOrdersUseCase listOrdersUseCase,
                        DeleteOrderUseCase deleteOrderUseCase,
                        OrderMapper orderMapper,
                        OrderCreatedProducer orderCreatedProducer) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.orderMapper = orderMapper;
        this.orderCreatedProducer = orderCreatedProducer;
    }

    public OrderResponse createOrder(OrderRequest request) {
        Order order = orderMapper.toEntity(request);
        Order savedOrder = createOrderUseCase.execute(order);
        // Envia evento para o Kafka
        orderCreatedProducer.send(new OrderCreatedEvent(savedOrder.getId(), savedOrder.getUserId(), savedOrder.getAmount()));
        return orderMapper.toResponse(savedOrder);
    }

    public Optional<OrderResponse> getOrderById(Long id) {
        return getOrderUseCase.execute(id)
                .map(orderMapper::toResponse);
    }

    public List<OrderResponse> getAllOrders() {
        return listOrdersUseCase.execute().stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteOrder(Long id) {
        deleteOrderUseCase.execute(id);
    }
}

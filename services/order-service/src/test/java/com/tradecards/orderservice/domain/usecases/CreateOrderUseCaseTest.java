package com.tradecards.orderservice.domain.usecases;

import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.domain.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute() {
        Order order = mock(Order.class);
        when(orderRepository.save(order)).thenReturn(order);
        Order result = createOrderUseCase.execute(order);
        assertEquals(order, result);
        verify(orderRepository).save(order);
    }
}

package com.tradecards.orderservice.domain.usecases;

import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.domain.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetOrderUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderUseCase getOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_found() {
        Order order = mock(Order.class);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Optional<Order> result = getOrderUseCase.execute(1L);
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    void testExecute_notFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Order> result = getOrderUseCase.execute(1L);
        assertFalse(result.isPresent());
    }
}

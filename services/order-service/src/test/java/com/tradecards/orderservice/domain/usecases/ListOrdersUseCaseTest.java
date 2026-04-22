package com.tradecards.orderservice.domain.usecases;

import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.domain.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListOrdersUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ListOrdersUseCase listOrdersUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute() {
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));
        List<Order> result = listOrdersUseCase.execute();
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));
    }
}

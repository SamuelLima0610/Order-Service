package com.tradecards.orderservice.domain.usecases;

import com.tradecards.orderservice.domain.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class DeleteOrderUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DeleteOrderUseCase deleteOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute() {
        doNothing().when(orderRepository).deleteById(1L);
        deleteOrderUseCase.execute(1L);
        verify(orderRepository).deleteById(1L);
    }
}

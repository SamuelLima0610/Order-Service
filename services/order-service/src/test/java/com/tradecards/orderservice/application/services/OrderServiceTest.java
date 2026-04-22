package com.tradecards.orderservice.application.services;

import com.tradecards.orderservice.application.dtos.OrderRequest;
import com.tradecards.orderservice.application.dtos.OrderResponse;
import com.tradecards.shared.events.OrderCreatedEvent;
import com.tradecards.orderservice.application.events.OrderCreatedProducer;
import com.tradecards.orderservice.application.mappers.OrderMapper;
import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.domain.usecases.CreateOrderUseCase;
import com.tradecards.orderservice.domain.usecases.DeleteOrderUseCase;
import com.tradecards.orderservice.domain.usecases.GetOrderUseCase;
import com.tradecards.orderservice.domain.usecases.ListOrdersUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Mock
    private CreateOrderUseCase createOrderUseCase;
    @Mock
    private GetOrderUseCase getOrderUseCase;
    @Mock
    private ListOrdersUseCase listOrdersUseCase;
    @Mock
    private DeleteOrderUseCase deleteOrderUseCase;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderCreatedProducer orderCreatedProducer;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderRequest request = mock(OrderRequest.class);
        Order order = mock(Order.class);
        Order savedOrder = mock(Order.class);
        OrderResponse response = mock(OrderResponse.class);
        when(orderMapper.toEntity(request)).thenReturn(order);
        when(createOrderUseCase.execute(order)).thenReturn(savedOrder);
        when(orderMapper.toResponse(savedOrder)).thenReturn(response);
        when(savedOrder.getId()).thenReturn(1L);
        when(savedOrder.getUserId()).thenReturn(2L);
        when(savedOrder.getAmount()).thenReturn(new java.math.BigDecimal("100.0"));

        OrderResponse result = orderService.createOrder(request);

        assertEquals(response, result);
        verify(orderCreatedProducer).send(any(OrderCreatedEvent.class));
    }

    @Test
    void testGetOrderById_found() {
        Order order = mock(Order.class);
        OrderResponse response = mock(OrderResponse.class);
        when(getOrderUseCase.execute(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(response);

        Optional<OrderResponse> result = orderService.getOrderById(1L);
        assertTrue(result.isPresent());
        assertEquals(response, result.get());
    }

    @Test
    void testGetOrderById_notFound() {
        when(getOrderUseCase.execute(1L)).thenReturn(Optional.empty());
        Optional<OrderResponse> result = orderService.getOrderById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllOrders() {
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        OrderResponse response1 = mock(OrderResponse.class);
        OrderResponse response2 = mock(OrderResponse.class);
        when(listOrdersUseCase.execute()).thenReturn(Arrays.asList(order1, order2));
        when(orderMapper.toResponse(order1)).thenReturn(response1);
        when(orderMapper.toResponse(order2)).thenReturn(response2);

        List<OrderResponse> result = orderService.getAllOrders();
        assertEquals(2, result.size());
        assertTrue(result.contains(response1));
        assertTrue(result.contains(response2));
    }
}
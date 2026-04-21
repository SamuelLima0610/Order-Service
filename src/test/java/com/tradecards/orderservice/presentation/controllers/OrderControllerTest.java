package com.tradecards.orderservice.presentation.controllers;

import com.tradecards.orderservice.application.dtos.OrderRequest;
import com.tradecards.orderservice.application.dtos.OrderResponse;
import com.tradecards.orderservice.application.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderRequest request = mock(OrderRequest.class);
        OrderResponse response = mock(OrderResponse.class);
        when(orderService.createOrder(request)).thenReturn(response);

        ResponseEntity<OrderResponse> result = orderController.createOrder(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetOrderById_found() {
        OrderResponse response = mock(OrderResponse.class);
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(response));

        ResponseEntity<OrderResponse> result = orderController.getOrderById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetOrderById_notFound() {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());
        ResponseEntity<OrderResponse> result = orderController.getOrderById(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void testGetAllOrders() {
        OrderResponse response1 = mock(OrderResponse.class);
        OrderResponse response2 = mock(OrderResponse.class);
        when(orderService.getAllOrders()).thenReturn(Arrays.asList(response1, response2));

        ResponseEntity<List<OrderResponse>> result = orderController.getAllOrders();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderService).deleteOrder(1L);
        ResponseEntity<Void> result = orderController.deleteOrder(1L);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }
}

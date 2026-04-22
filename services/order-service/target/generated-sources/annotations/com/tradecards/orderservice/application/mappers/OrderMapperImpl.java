package com.tradecards.orderservice.application.mappers;

import com.tradecards.orderservice.application.dtos.OrderRequest;
import com.tradecards.orderservice.application.dtos.OrderResponse;
import com.tradecards.orderservice.domain.entities.Order;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-22T11:47:52-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Debian)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId( order.getId() );
        orderResponse.setUserId( order.getUserId() );
        orderResponse.setAmount( order.getAmount() );
        orderResponse.setStatus( order.getStatus() );
        orderResponse.setCreatedAt( order.getCreatedAt() );
        orderResponse.setUpdatedAt( order.getUpdatedAt() );

        return orderResponse;
    }

    @Override
    public Order toEntity(OrderRequest request) {
        if ( request == null ) {
            return null;
        }

        Order order = new Order();

        order.setUserId( request.getUserId() );
        order.setAmount( request.getAmount() );
        order.setStatus( request.getStatus() );

        return order;
    }
}

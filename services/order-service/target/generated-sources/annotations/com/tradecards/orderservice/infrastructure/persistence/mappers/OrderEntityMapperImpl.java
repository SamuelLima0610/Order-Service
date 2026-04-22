package com.tradecards.orderservice.infrastructure.persistence.mappers;

import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.infrastructure.persistence.entities.OrderEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-22T11:47:52-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Debian)"
)
@Component
public class OrderEntityMapperImpl implements OrderEntityMapper {

    @Override
    public Order toDomain(OrderEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Order order = new Order();

        order.setId( entity.getId() );
        order.setUserId( entity.getUserId() );
        order.setAmount( entity.getAmount() );
        order.setStatus( entity.getStatus() );
        order.setCreatedAt( entity.getCreatedAt() );
        order.setUpdatedAt( entity.getUpdatedAt() );

        return order;
    }

    @Override
    public OrderEntity toEntity(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setId( order.getId() );
        orderEntity.setUserId( order.getUserId() );
        orderEntity.setAmount( order.getAmount() );
        orderEntity.setStatus( order.getStatus() );
        orderEntity.setCreatedAt( order.getCreatedAt() );
        orderEntity.setUpdatedAt( order.getUpdatedAt() );

        return orderEntity;
    }
}

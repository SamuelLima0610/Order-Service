package com.tradecards.orderservice.infrastructure.persistence.mappers;

import com.tradecards.orderservice.domain.entities.Order;
import com.tradecards.orderservice.infrastructure.persistence.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderEntityMapper {
    
    Order toDomain(OrderEntity entity);
    
    OrderEntity toEntity(Order order);
}

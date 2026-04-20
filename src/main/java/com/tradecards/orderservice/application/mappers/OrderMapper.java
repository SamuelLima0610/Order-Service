package com.tradecards.orderservice.application.mappers;

import com.tradecards.orderservice.application.dtos.OrderRequest;
import com.tradecards.orderservice.application.dtos.OrderResponse;
import com.tradecards.orderservice.domain.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    
    OrderResponse toResponse(Order order);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderRequest request);
}

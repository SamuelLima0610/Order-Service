package com.tradecards.paymentservice.infrastructure.persistence.mappers;

import com.tradecards.paymentservice.domain.entities.Payment;
import com.tradecards.paymentservice.infrastructure.persistence.entities.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentEntityMapper {

    Payment toDomain(PaymentEntity entity);

    PaymentEntity toEntity(Payment payment);
}

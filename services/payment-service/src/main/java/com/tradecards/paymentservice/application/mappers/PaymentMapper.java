package com.tradecards.paymentservice.application.mappers;

import com.tradecards.paymentservice.application.dtos.PaymentResponse;
import com.tradecards.paymentservice.domain.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    PaymentResponse toResponse(Payment payment);
}

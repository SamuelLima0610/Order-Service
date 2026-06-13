package com.tradecards.paymentservice.infrastructure.persistence.mappers;

import com.tradecards.paymentservice.domain.entities.Payment;
import com.tradecards.paymentservice.infrastructure.persistence.entities.PaymentEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-28T18:14:57-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Debian)"
)
@Component
public class PaymentEntityMapperImpl implements PaymentEntityMapper {

    @Override
    public Payment toDomain(PaymentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Payment payment = new Payment();

        payment.setId( entity.getId() );
        payment.setOrderId( entity.getOrderId() );
        payment.setUserId( entity.getUserId() );
        payment.setAmount( entity.getAmount() );
        payment.setStatus( entity.getStatus() );
        payment.setCreatedAt( entity.getCreatedAt() );
        payment.setUpdatedAt( entity.getUpdatedAt() );

        return payment;
    }

    @Override
    public PaymentEntity toEntity(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setId( payment.getId() );
        paymentEntity.setOrderId( payment.getOrderId() );
        paymentEntity.setUserId( payment.getUserId() );
        paymentEntity.setAmount( payment.getAmount() );
        paymentEntity.setStatus( payment.getStatus() );
        paymentEntity.setCreatedAt( payment.getCreatedAt() );
        paymentEntity.setUpdatedAt( payment.getUpdatedAt() );

        return paymentEntity;
    }
}

package com.tradecards.paymentservice.application.mappers;

import com.tradecards.paymentservice.application.dtos.PaymentResponse;
import com.tradecards.paymentservice.domain.entities.Payment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-28T18:14:57-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Debian)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponse toResponse(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentResponse paymentResponse = new PaymentResponse();

        paymentResponse.setId( payment.getId() );
        paymentResponse.setOrderId( payment.getOrderId() );
        paymentResponse.setUserId( payment.getUserId() );
        paymentResponse.setAmount( payment.getAmount() );
        paymentResponse.setStatus( payment.getStatus() );
        paymentResponse.setCreatedAt( payment.getCreatedAt() );
        paymentResponse.setUpdatedAt( payment.getUpdatedAt() );

        return paymentResponse;
    }
}

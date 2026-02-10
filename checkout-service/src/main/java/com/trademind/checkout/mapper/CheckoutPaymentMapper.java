package com.trademind.checkout.mapper;

import com.trademind.checkout.dto.response.CheckoutPaymentResponseDto;
import com.trademind.checkout.entity.CheckoutPaymentSnapshot;
import org.springframework.stereotype.Component;

@Component
public class CheckoutPaymentMapper {

    public CheckoutPaymentResponseDto toResponseDto(
            CheckoutPaymentSnapshot payment
    ) {
        if (payment == null) {
            return null;
        }

        return new CheckoutPaymentResponseDto(
                payment.getPaymentMethod(),
                payment.getPaymentProvider(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus()
        );
    }
}

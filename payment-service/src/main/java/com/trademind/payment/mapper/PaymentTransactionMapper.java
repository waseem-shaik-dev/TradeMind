package com.trademind.payment.mapper;

import com.trademind.payment.dto.response.PaymentResponseDto;
import com.trademind.payment.dto.response.PaymentSummaryResponseDto;
import com.trademind.payment.entity.PaymentTransaction;
import org.springframework.stereotype.Component;

@Component
public class PaymentTransactionMapper {

    public PaymentResponseDto toResponseDto(PaymentTransaction payment) {

        return new PaymentResponseDto(
                payment.getId(),
                payment.getCheckoutId(),

                payment.getAmount(),
                payment.getCurrency(),

                payment.getPaymentMethod(),
                payment.getPaymentProvider(),
                payment.getStatus(),

                payment.getFailureReason(),
                payment.getCreatedAt()
        );
    }

    public PaymentSummaryResponseDto toSummaryDto(
            PaymentTransaction payment
    ) {
        return new PaymentSummaryResponseDto(
                payment.getId(),
                payment.getCheckoutId(),
                payment.getStatus()
        );
    }
}

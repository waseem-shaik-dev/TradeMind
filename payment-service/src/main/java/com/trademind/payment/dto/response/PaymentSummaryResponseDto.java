package com.trademind.payment.dto.response;

import com.trademind.payment.enums.PaymentStatus;

public record PaymentSummaryResponseDto(

        Long paymentId,
        Long checkoutId,
        PaymentStatus status
) {}

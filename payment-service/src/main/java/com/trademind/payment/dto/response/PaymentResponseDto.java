package com.trademind.payment.dto.response;

import com.trademind.payment.enums.PaymentMethod;
import com.trademind.payment.enums.PaymentProvider;
import com.trademind.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDto(

        Long paymentId,
        Long checkoutId,

        BigDecimal amount,
        String currency,

        PaymentMethod paymentMethod,
        PaymentProvider paymentProvider,
        PaymentStatus status,

        String failureReason,

        LocalDateTime createdAt
) {}

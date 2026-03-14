package com.trademind.payment.dto.request;

import com.trademind.payment.enums.PaymentMethod;

import java.math.BigDecimal;

public record CreatePaymentRequestDto(

        Long checkoutId,
        Long userId,

        BigDecimal amount,
        String currency,

        PaymentMethod paymentMethod
) {}

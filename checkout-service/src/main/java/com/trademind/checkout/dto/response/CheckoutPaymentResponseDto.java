package com.trademind.checkout.dto.response;

import com.trademind.checkout.enums.PaymentMethod;
import com.trademind.checkout.enums.PaymentStatus;

import java.math.BigDecimal;

public record CheckoutPaymentResponseDto(

        PaymentMethod paymentMethod,
        String paymentProvider,

        BigDecimal amount,
        String currency,

        PaymentStatus status
) {}

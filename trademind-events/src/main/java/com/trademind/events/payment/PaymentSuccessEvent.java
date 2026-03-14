package com.trademind.events.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentSuccessEvent(

        String eventId,
        Long paymentId,
        Long checkoutId,
        Long userId,

        BigDecimal amount,
        String currency,

        String paymentProvider,
        String providerPaymentIntentId,

        LocalDateTime occurredAt
) {}

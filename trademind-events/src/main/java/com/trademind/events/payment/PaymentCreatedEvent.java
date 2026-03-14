package com.trademind.events.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentCreatedEvent(

        String eventId,
        Long paymentId,
        Long checkoutId,
        Long userId,

        BigDecimal amount,
        String currency,

        String paymentMethod,   // COD / ONLINE
        String paymentProvider, // STRIPE / RAZORPAY

        LocalDateTime occurredAt
) {}

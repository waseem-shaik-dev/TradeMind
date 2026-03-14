package com.trademind.events.payment;

import java.time.LocalDateTime;

public record PaymentCancelledEvent(

        String eventId,
        Long paymentId,
        Long checkoutId,
        Long userId,

        String reason,

        LocalDateTime occurredAt
) {}

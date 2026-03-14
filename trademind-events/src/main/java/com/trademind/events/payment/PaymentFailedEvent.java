package com.trademind.events.payment;

import java.time.LocalDateTime;

public record PaymentFailedEvent(

        String eventId,
        Long paymentId,
        Long checkoutId,
        Long userId,

        String reason,
        String paymentProvider,

        LocalDateTime occurredAt
) {}

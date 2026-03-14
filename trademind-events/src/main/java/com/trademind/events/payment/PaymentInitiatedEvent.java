package com.trademind.events.payment;

import java.time.LocalDateTime;

public record PaymentInitiatedEvent(

        String eventId,
        Long paymentId,
        Long checkoutId,

        String paymentProvider,
        String providerPaymentIntentId,

        LocalDateTime occurredAt
) {}

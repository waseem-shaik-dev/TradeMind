package com.trademind.events.order;

import com.trademind.events.checkout.common.EventMetadata;

import java.time.LocalDateTime;

public record OrderPaymentUpdatedEvent(

        EventMetadata metadata,

        Long checkoutId,
        Long orderId,

        PaymentStatus paymentStatus,

        String transactionId,

        LocalDateTime updatedAt

) {}

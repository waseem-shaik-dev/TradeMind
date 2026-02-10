package com.trademind.events.checkout;

import com.trademind.events.checkout.common.EventMetadata;

public record CheckoutCancelledEvent(

        EventMetadata metadata,

        Long checkoutId,
        Long userId,

        String reason
) {}

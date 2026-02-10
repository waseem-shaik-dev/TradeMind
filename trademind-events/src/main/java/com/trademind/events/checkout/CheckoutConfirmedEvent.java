package com.trademind.events.checkout;

import com.trademind.events.checkout.common.EventMetadata;

public record CheckoutConfirmedEvent(

        EventMetadata metadata,

        Long checkoutId,
        Long userId,

        String paymentMethod,   // COD / ONLINE
        String currency
) {}

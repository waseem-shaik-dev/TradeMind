package com.trademind.events.checkout.common;

import java.time.LocalDateTime;

public record EventMetadata(

        String eventId,          // UUID for idempotency
        String eventType,        // INVENTORY_RESERVE_REQUESTED, etc.
        String sourceService,    // checkout-service
        LocalDateTime createdAt,
        int version              // start with 1
) {}

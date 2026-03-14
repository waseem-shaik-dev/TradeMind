package com.trademind.events.inventory;

import com.trademind.events.checkout.common.EventMetadata;

public record InventoryReserveFailedEvent(

        EventMetadata metadata,

        Long checkoutId,

        String reason   // e.g. INSUFFICIENT_STOCK, STOCK_NOT_FOUND
) {}

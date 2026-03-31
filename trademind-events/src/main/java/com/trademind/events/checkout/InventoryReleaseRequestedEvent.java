package com.trademind.events.checkout;

import com.trademind.events.checkout.common.EventMetadata;
import com.trademind.events.checkout.common.ItemQuantityDto;

import java.util.List;

public record InventoryReleaseRequestedEvent(

        EventMetadata metadata,

        Long checkoutId,
        Long sourceId,
        String sourceRole,

        List<ItemQuantityDto> items,

        String reason
) {}

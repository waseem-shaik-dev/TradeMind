package com.trademind.events.checkout;

import com.trademind.events.checkout.common.EventMetadata;
import com.trademind.events.checkout.common.ItemQuantityDto;

import java.util.List;

public record InventoryReserveRequestedEvent(

        EventMetadata metadata,

        Long checkoutId,
        Long userId,

        List<ItemQuantityDto> items
) {}

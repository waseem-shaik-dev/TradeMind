package com.trademind.events.order;

import com.trademind.events.checkout.common.EventMetadata;

import java.time.LocalDateTime;

public record OrderStatusUpdatedEvent(

        EventMetadata metadata,

        Long orderId,
        OrderStatus orderStatus,

        String reason,

        LocalDateTime updatedAt

) {}

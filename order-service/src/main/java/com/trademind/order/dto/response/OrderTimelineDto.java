package com.trademind.order.dto.response;

import com.trademind.order.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderTimelineDto(

        OrderStatus status,
        LocalDateTime updatedAt

) {}

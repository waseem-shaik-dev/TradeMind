package com.trademind.order.dto.response;

import com.trademind.order.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusUpdateResponseDto(

        Long orderId,
        OrderStatus orderStatus,
        String message,
        LocalDateTime updatedAt

) {}

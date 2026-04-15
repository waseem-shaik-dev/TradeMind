package com.trademind.order.dto.response;

import com.trademind.order.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderTrackingStepDto(

        OrderStatus status,
        boolean completed,
        boolean current,
        boolean upcoming,
        LocalDateTime timestamp

) {}
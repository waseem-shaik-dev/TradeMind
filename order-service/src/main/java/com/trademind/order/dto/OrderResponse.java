package com.trademind.order.dto;

import com.trademind.merchantorder.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderResponse(
        Long orderId,
        BigDecimal totalAmount,
        OrderStatus status
) {}


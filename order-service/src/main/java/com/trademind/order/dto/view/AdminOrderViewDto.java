package com.trademind.order.dto.view;

import com.trademind.order.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminOrderViewDto(

        Long orderId,
        String orderNumber,

        Long userId,
        Long sourceId,
        SourceType sourceType,

        OrderStatus orderStatus,
        PaymentStatus paymentStatus,

        BigDecimal grandTotal,

        LocalDateTime createdAt

) {}

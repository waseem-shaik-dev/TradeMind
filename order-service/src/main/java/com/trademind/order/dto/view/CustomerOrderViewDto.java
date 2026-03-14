package com.trademind.order.dto.view;

import com.trademind.order.enums.OrderStatus;
import com.trademind.order.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerOrderViewDto(

        Long orderId,
        String orderNumber,

        OrderStatus orderStatus,
        PaymentStatus paymentStatus,

        BigDecimal grandTotal,
        String currency,

        LocalDateTime createdAt

) {}

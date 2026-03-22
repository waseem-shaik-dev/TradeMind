package com.trademind.events.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderBillingEvent(

        Long orderId,
        String orderNumber,

        Long userId,
        Long sourceId,
        String sourceType,

        String paymentMethod,
        String paymentStatus,

        BigDecimal subtotalAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,
        String currency,

        OrderAddressDto address,

        List<OrderItemDto> items

) {}
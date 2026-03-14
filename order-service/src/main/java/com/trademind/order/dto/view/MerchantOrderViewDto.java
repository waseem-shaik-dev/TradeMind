package com.trademind.order.dto.view;

import com.trademind.order.enums.OrderStatus;
import com.trademind.order.enums.DeliveryType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MerchantOrderViewDto(

        Long orderId,
        String orderNumber,

        Long customerId,

        DeliveryType deliveryType,
        OrderStatus orderStatus,

        BigDecimal grandTotal,

        LocalDateTime createdAt

) {}

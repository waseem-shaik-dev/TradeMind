package com.trademind.order.dto.response;

import com.trademind.events.common.SellerSnapshotDto;
import com.trademind.order.enums.OrderStatus;
import com.trademind.order.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryResponseDto(

        Long id,
        String orderNumber,

        Long userId,
        Long sourceId,

        OrderStatus orderStatus,
        PaymentStatus paymentStatus,

        BigDecimal grandTotal,
        String currency,

        LocalDateTime createdAt,

        SellerSnapshotDto seller

) {}

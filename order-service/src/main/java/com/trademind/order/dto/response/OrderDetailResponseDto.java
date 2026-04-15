package com.trademind.order.dto.response;

import com.trademind.events.common.SellerSnapshotDto;
import com.trademind.order.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponseDto(

        Long id,
        String orderNumber,

        Long checkoutId,
        Long cartId,
        Long userId,

        Long sourceId,
        SourceType sourceType,

        DeliveryType deliveryType,

        OrderStatus orderStatus,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,

        BigDecimal subtotalAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,
        String currency,

        OrderAddressDto address,

        List<OrderLineItemDto> items,

        SellerSnapshotDto seller,

        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}

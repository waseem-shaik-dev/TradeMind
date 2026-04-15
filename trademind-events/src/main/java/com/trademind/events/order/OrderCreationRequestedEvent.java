package com.trademind.events.order;

import com.trademind.events.checkout.common.EventMetadata;
import com.trademind.events.common.SellerSnapshotDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderCreationRequestedEvent(

        EventMetadata metadata,

        // --- Checkout reference ---
        Long checkoutId,
        Long cartId,
        Long userId,
        String userEmail,
        String buyerType,

        // --- Merchant / Source ---
        Long sourceId,
        String sourceType,

        // --- Delivery ---
        DeliveryType deliveryType,

        // --- Address Snapshot ---
        OrderAddressDto address,

        // --- Pricing ---
        BigDecimal subtotalAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,
        String currency,

        // --- Payment ---
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,

        // --- Items ---
        List<OrderItemDto> items,

        SellerSnapshotDto seller,

        LocalDateTime createdAt

) {}

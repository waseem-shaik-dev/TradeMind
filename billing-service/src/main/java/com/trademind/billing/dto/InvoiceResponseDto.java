package com.trademind.billing.dto;

import com.trademind.billing.enums.PaymentMethod;
import com.trademind.billing.enums.PaymentStatus;
import com.trademind.billing.enums.SourceType;
import com.trademind.events.common.SellerSnapshotDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceResponseDto(

        Long id,
        String invoiceNumber,

        Long orderId,
        String orderNumber,

        Long userId,
        Long sourceId,
        SourceType sourceType,

        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,

        BigDecimal subtotal,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,
        String currency,

        String addressSnapshot,
        SellerSnapshotDto seller,

        List<InvoiceLineItemDto> items,

        String invoiceHash,

        LocalDateTime createdAt

) {}
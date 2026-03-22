package com.trademind.billing.dto;

import com.trademind.billing.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceSummaryDto(

        Long id,
        String invoiceNumber,

        String orderNumber,

        BigDecimal grandTotal,
        String currency,

        PaymentStatus paymentStatus,

        LocalDateTime createdAt

) {}
package com.trademind.billing.dto;

import java.math.BigDecimal;

public record InvoiceLineItemDto(

        Long productId,
        String productName,
        String sku,
        String imageUrl,

        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice

) {}
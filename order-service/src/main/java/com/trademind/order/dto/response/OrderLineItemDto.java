package com.trademind.order.dto.response;

import java.math.BigDecimal;

public record OrderLineItemDto(

        Long productId,
        String productName,
        String sku,
        String imageUrl,

        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice

) {}

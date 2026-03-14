package com.trademind.events.order;

import java.math.BigDecimal;

public record OrderItemDto(

        Long productId,
        String productName,
        String sku,
        String imageUrl,

        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice

) {}

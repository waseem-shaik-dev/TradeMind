package com.trademind.checkout.dto.response;

import java.math.BigDecimal;

public record CheckoutItemResponseDto(

        Long productId,
        String productName,
        String sku,

        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice
) {}

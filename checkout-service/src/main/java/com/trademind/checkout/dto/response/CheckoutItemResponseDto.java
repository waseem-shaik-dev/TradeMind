package com.trademind.checkout.dto.response;

import java.math.BigDecimal;

public record CheckoutItemResponseDto(

        Long productId,
        String productName,
        String sku,
        String imageUrl,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice
) {}

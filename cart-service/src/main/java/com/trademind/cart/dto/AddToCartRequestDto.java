package com.trademind.cart.dto;

import java.math.BigDecimal;

public record AddToCartRequestDto(
        Long sourceId,        // merchantId or retailerId
        String sourceRole,    // MERCHANT / RETAILER
        Long productId,
        Integer quantity,
        BigDecimal price
) {}

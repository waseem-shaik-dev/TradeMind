package com.trademind.cart.dto;

public record AddToCartRequestDto(
        Long sourceId,        // merchantId or retailerId
        String sourceType,    // MERCHANT / RETAILER
        Long productId,
        Integer quantity
) {}

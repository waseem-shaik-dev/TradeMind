package com.trademind.cart.dto;

public record CartSourceDto(
        Long sourceId,
        String sourceType,   // MERCHANT / RETAILER

        String sourceName,
        String sourceLogo
) {
}

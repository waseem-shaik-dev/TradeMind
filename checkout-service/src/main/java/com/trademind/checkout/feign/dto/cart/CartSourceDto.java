package com.trademind.checkout.feign.dto.cart;

public record CartSourceDto(
        Long sourceId,
        String sourceType,   // MERCHANT / RETAILER

        String sourceName,
        String sourceLogo
) {
}

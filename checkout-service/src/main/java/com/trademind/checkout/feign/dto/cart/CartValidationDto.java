package com.trademind.checkout.feign.dto.cart;

public record CartValidationDto(
        boolean valid,

        boolean hasOutOfStockItems,
        boolean hasPriceMismatch,
        boolean exceedsQuantityLimit,

        String message
) {
}

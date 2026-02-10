package com.trademind.cart.dto;

public record CartValidationDto(
        boolean valid,

        boolean hasOutOfStockItems,
        boolean hasPriceMismatch,
        boolean exceedsQuantityLimit,

        String message
) {
}

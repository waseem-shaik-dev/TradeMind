package com.trademind.checkout.feign.dto.cart;

import java.time.LocalDateTime;
import java.util.List;

public record CartResponseDto(
        Long cartId,
        Long userId,

        CartSourceDto source,

        String status,     // ACTIVE / CHECKED_OUT / EXPIRED
        boolean active,

        List<CartItemResponseDto> items,

        CartPriceSummaryDto priceSummary,
        CartValidationDto validation,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

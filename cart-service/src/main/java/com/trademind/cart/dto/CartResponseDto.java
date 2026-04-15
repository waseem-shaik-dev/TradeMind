package com.trademind.cart.dto;

import com.trademind.events.common.SellerSnapshotDto;

import java.time.LocalDateTime;
import java.util.List;

public record CartResponseDto(
        Long cartId,
        Long userId,

        SellerSnapshotDto seller,

        String status,     // ACTIVE / CHECKED_OUT / EXPIRED
        boolean active,

        List<CartItemResponseDto> items,

        CartPriceSummaryDto priceSummary,
        CartValidationDto validation,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

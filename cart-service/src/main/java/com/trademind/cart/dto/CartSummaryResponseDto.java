package com.trademind.cart.dto;

import com.trademind.events.common.SellerSnapshotDto;

import java.math.BigDecimal;

public record CartSummaryResponseDto(
        Long cartId,

        SellerSnapshotDto seller,

        Integer totalItems,
        Integer totalQuantity,

        BigDecimal grandTotal,

        boolean active
) {
}

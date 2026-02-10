package com.trademind.cart.dto;

import java.math.BigDecimal;

public record CartSummaryResponseDto(
        Long cartId,

        CartSourceDto source,

        Integer totalItems,
        Integer totalQuantity,

        BigDecimal grandTotal,

        boolean active
) {
}

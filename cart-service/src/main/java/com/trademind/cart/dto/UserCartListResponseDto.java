package com.trademind.cart.dto;

import java.util.List;

public record UserCartListResponseDto(
        Long userId,

        Integer maxAllowedCarts,
        Integer currentCartCount,

        List<CartSummaryResponseDto> carts
) {
}

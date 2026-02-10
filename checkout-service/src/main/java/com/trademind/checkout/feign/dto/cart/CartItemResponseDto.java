package com.trademind.checkout.feign.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartItemResponseDto(
        Long cartItemId,

        Long productId,
        String productName,
        String sku,

        String primaryImage,
        List<String> images,

        BigDecimal unitPrice,
        Integer quantity,

        BigDecimal totalPrice,

        boolean available,
        boolean priceChanged,
        boolean outOfStock
) {
}

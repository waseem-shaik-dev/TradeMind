package com.trademind.checkout.feign.dto.cart;

import java.math.BigDecimal;

public record CartItemDto(

        Long productId,
        String productName,
        String sku,

        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice
) {}

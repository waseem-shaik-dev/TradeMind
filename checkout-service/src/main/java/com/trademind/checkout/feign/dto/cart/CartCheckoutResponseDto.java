package com.trademind.checkout.feign.dto.cart;

import com.trademind.checkout.enums.SourceType;

import java.math.BigDecimal;
import java.util.List;

public record CartCheckoutResponseDto(

        Long cartId,
        Long userId,

        Long sourceId,
        SourceType sourceType,

        List<CartItemDto> items,

        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal discount,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,

        String currency
) {}

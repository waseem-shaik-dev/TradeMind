package com.trademind.checkout.feign.dto.cart;

import java.math.BigDecimal;

public record CartPriceSummaryDto(
        BigDecimal subTotal,
        BigDecimal tax,
        BigDecimal discount,
        BigDecimal grandTotal,

        Integer totalItems,
        Integer totalQuantity
) {
}

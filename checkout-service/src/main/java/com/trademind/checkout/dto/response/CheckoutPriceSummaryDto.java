package com.trademind.checkout.dto.response;

import java.math.BigDecimal;

public record CheckoutPriceSummaryDto(

        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal discount,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,

        Integer totalItems,
        Integer totalQuantity
) {}

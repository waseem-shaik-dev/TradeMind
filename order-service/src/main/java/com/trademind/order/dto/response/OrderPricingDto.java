package com.trademind.order.dto.response;

import java.math.BigDecimal;

public record OrderPricingDto(

        BigDecimal subtotalAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,
        String currency

) {}

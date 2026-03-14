package com.trademind.payment.feign.dto;

import java.math.BigDecimal;

public record CheckoutPaymentViewDto(
        Long checkoutId,
        Long userId,
        BigDecimal amount,
        String currency
) {}

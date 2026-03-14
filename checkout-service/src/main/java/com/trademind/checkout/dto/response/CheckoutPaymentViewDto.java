package com.trademind.checkout.dto.response;

import java.math.BigDecimal;

public record CheckoutPaymentViewDto(
        Long checkoutId,
        Long userId,
        BigDecimal amount,
        String currency,
        String status   // CREATED / RESERVED / etc
) {}

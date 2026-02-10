package com.trademind.checkout.dto.response;

import com.trademind.checkout.enums.CheckoutStatus;

public record CheckoutSummaryResponseDto(

        Long checkoutId,
        CheckoutStatus status
) {}

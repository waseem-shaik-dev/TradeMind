package com.trademind.checkout.dto.response;

import com.trademind.checkout.enums.BuyerType;
import com.trademind.checkout.enums.CheckoutStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CheckoutResponseDto(

        Long checkoutId,
        Long cartId,

        CheckoutStatus status,

        CheckoutAddressResponseDto address,
        CheckoutPaymentResponseDto payment,

        List<CheckoutItemResponseDto> items,
        CheckoutPriceSummaryDto priceSummary,

        LocalDateTime expiresAt
) {}

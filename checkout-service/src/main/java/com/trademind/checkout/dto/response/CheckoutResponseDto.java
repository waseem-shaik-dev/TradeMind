package com.trademind.checkout.dto.response;

import com.trademind.checkout.enums.BuyerType;
import com.trademind.checkout.enums.CheckoutStatus;
import com.trademind.events.common.SellerSnapshotDto;

import java.time.LocalDateTime;
import java.util.List;

public record CheckoutResponseDto(

        Long checkoutId,
        Long cartId,

        CheckoutStatus status,

        SellerSnapshotDto seller,

        CheckoutAddressResponseDto address,
        CheckoutPaymentResponseDto payment,

        List<CheckoutItemResponseDto> items,
        CheckoutPriceSummaryDto priceSummary,

        LocalDateTime expiresAt
) {}

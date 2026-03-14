package com.trademind.checkout.dto.response;

import com.trademind.checkout.enums.CheckoutStatus;
import com.trademind.checkout.enums.DeliveryType;

import java.math.BigDecimal;

public record DeliveryTypeSelectionResponseDto(

        Long checkoutId,
        DeliveryType deliveryType,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,
        CheckoutStatus status

) {}

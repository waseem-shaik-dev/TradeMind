package com.trademind.checkout.dto.request;

import com.trademind.checkout.enums.DeliveryType;

public record SelectDeliveryTypeRequestDto(
        Long checkoutId,
        DeliveryType deliveryType
) {}

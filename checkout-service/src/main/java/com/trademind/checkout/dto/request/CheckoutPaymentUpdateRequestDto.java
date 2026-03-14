package com.trademind.checkout.dto.request;

public record CheckoutPaymentUpdateRequestDto(
        String newStatus   // CONFIRMED / CANCELLED
) {}

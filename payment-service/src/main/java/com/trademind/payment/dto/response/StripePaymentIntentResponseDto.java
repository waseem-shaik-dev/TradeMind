package com.trademind.payment.dto.response;

public record StripePaymentIntentResponseDto(

        Long paymentId,

        String clientSecret,
        String publishableKey
) {}

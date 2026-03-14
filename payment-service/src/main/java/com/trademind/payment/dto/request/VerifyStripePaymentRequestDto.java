package com.trademind.payment.dto.request;

public record VerifyStripePaymentRequestDto(

        String paymentIntentId,
        String chargeId
) {}

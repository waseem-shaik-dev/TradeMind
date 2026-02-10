package com.trademind.checkout.dto.request;

import com.trademind.checkout.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record SelectPaymentMethodRequestDto(

        @NotNull
        Long checkoutId,

        @NotNull
        PaymentMethod paymentMethod
) {}

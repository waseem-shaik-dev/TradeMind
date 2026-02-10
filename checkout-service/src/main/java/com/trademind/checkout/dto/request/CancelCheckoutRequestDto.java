package com.trademind.checkout.dto.request;

import jakarta.validation.constraints.NotNull;

public record CancelCheckoutRequestDto(

        @NotNull
        Long checkoutId,

        String reason
) {}

package com.trademind.checkout.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateCheckoutRequestDto(

        @NotNull
        Long cartId
) {}

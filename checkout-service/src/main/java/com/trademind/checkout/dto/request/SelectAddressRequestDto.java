package com.trademind.checkout.dto.request;

import jakarta.validation.constraints.NotNull;

public record SelectAddressRequestDto(

        @NotNull
        Long checkoutId,

        @NotNull
        Long addressId
) {}

package com.trademind.checkout.dto.response;

public record CheckoutAddressResponseDto(

        String fullName,
        String phone,

        String addressLine1,
        String addressLine2,

        String city,
        String state,
        String postalCode,
        String country
) {}

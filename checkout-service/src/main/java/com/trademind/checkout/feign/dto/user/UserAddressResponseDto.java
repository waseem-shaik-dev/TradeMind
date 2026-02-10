package com.trademind.checkout.feign.dto.user;

public record UserAddressResponseDto(

        Long addressId,

        String fullName,
        String phone,

        String addressLine1,
        String addressLine2,

        String city,
        String state,
        String postalCode,
        String country
) {}

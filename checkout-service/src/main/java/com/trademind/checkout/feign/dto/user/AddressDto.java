package com.trademind.checkout.feign.dto.user;

public record AddressDto(
        Long id,

        String fullName,
        String phone,

        String line1,
        String line2,

        String city,
        String state,
        String pincode,
        String country
) {}

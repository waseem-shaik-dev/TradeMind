package com.trademind.order.dto.response;

public record OrderAddressDto(

        String fullName,
        String phone,

        String addressLine1,
        String addressLine2,

        String city,
        String state,
        String postalCode,
        String country

) {}

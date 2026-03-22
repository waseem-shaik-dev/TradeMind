package com.trademind.user.dto;

public record StoreAddressDto(

        Long id,

        String line1,
        String line2,

        String city,
        String state,
        String pincode,
        String country,

        Double latitude,
        Double longitude,

        String mapUrl

) {}
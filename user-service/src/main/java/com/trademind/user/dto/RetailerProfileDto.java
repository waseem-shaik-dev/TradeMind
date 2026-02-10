package com.trademind.user.dto;

public record RetailerProfileDto(
        String shopName,
        String shopEmail,
        String gstNumber,
        Double latitude,
        Double longitude,
        String mapUrl,
        Boolean verified
) {}

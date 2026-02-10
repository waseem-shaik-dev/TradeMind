package com.trademind.user.dto;

public record MerchantProfileDto(
        String businessName,
        String businessEmail,
        String gstNumber,
        String licenseNumber,
        Double latitude,
        Double longitude,
        String mapUrl,
        Boolean verified
) {}

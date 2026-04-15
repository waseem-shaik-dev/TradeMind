package com.trademind.user.dto;

public record MerchantProfileDto(
        String businessName,
        String businessEmail,
        String shopImageUrl,
        StoreAddressDto storeAddress
) {}

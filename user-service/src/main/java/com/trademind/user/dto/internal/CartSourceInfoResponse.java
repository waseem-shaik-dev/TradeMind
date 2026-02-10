package com.trademind.user.dto.internal;

public record CartSourceInfoResponse(
        Long sourceId,
        String sourceType, // MERCHANT / RETAILER
        String name,       // businessName or shopName
        String logo        // avatarUrl
) {
}

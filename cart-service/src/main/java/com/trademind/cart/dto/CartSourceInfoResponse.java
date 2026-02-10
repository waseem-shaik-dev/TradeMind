package com.trademind.cart.dto;

public record CartSourceInfoResponse(
        Long sourceId,
        String sourceType, // MERCHANT / RETAILER
        String name,       // businessName or shopName
        String logo        // avatarUrl
) {
}
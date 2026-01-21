package com.trademind.product.dto;

public record ProductImageUploadResponse(
        Long imageId,
        String imageUrl,
        boolean primary
) {}

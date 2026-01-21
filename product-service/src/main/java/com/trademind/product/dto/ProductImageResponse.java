package com.trademind.product.dto;

public record ProductImageResponse(
        Long id,
        String imageUrl,
        boolean primary,
        Integer displayOrder
) {}

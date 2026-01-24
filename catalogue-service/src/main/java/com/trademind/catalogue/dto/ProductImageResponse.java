package com.trademind.catalogue.dto;

public record ProductImageResponse(
        Long id,
        String imageUrl,
        boolean primary,
        Integer displayOrder
) {}

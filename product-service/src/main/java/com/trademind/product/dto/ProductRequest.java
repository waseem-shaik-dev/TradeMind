package com.trademind.product.dto;

public record ProductRequest(
        String name,
        String sku,
        String description,
        Long categoryId,
        Long brandId,
        Long unitOfMeasureId
) {}


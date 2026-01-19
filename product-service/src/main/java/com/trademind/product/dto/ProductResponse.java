package com.trademind.product.dto;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        boolean active
) {}


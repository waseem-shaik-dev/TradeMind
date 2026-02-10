package com.trademind.inventory.dto;

public record InventoryAvailabilityResponse(
        Long productId,
        Integer quantityAvailable,
        boolean outOfStock
) {}

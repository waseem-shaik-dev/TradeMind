package com.trademind.inventory.dto;

public record CheckoutItemDto(
        Long productId,
        Integer quantity
) {}

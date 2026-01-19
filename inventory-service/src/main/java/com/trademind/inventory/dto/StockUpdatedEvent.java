package com.trademind.inventory.dto;

public record StockUpdatedEvent(
        Long productId,
        Integer quantityAvailable
) {}

package com.trademind.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "inventory-client",
        url = "http://localhost:8084"
)
public interface InventoryClient {

    @GetMapping("/api/inventories/internal/{productId}/available")
    Integer getAvailableQuantity(@PathVariable Long productId);

    @PostMapping("/api/inventories/internal/availability")
    List<InventoryAvailabilityDto> getAvailabilityForProducts(
            @RequestBody List<Long> productIds
    );

    record InventoryAvailabilityDto(
            Long productId,
            Integer quantityAvailable,
            boolean outOfStock
    ) {}
}

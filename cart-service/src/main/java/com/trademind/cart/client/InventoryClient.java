package com.trademind.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "inventory-client",
        url = "http://localhost:8084"
)
public interface InventoryClient {

    @GetMapping("/api/inventories/internal/available")
    Integer getAvailableQuantity(@RequestParam Long productId,
                                 @RequestParam Long sellerId,
                                 @RequestParam String sellerRole
                                 );

    @PostMapping("/api/inventories/internal/seller/{sellerId}/availability")
    List<InventoryAvailabilityDto> getAvailabilityForProducts(
            @PathVariable Long sellerId,
            @RequestBody List<Long> productIds
    );

    record InventoryAvailabilityDto(
            Long productId,
            Integer quantityAvailable,
            boolean outOfStock
    ) {}
}

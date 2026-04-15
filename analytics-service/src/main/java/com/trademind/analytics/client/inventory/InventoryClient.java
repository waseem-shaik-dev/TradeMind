package com.trademind.analytics.client.inventory;

import com.trademind.analytics.client.inventory.dto.InventorySummaryDto;
import com.trademind.analytics.client.inventory.dto.LowStockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "inventory-service",
        url = "http://localhost:8084",
        configuration = com.trademind.analytics.client.config.FeignConfig.class
)
public interface InventoryClient {

    @GetMapping("/api/inventories/low-stock")
    List<LowStockResponse> getLowStockProducts(@RequestParam("sellerId") Long sellerId);

    @GetMapping("/api/inventories/count/low-stock")
    int getLowStockCount(@RequestParam Long sellerId);

    @GetMapping("/api/inventories/count/out-of-stock")
    int getOutOfStockCount(@RequestParam Long sellerId);

    @GetMapping("/api/inventories/summary")
    InventorySummaryDto getSummary(@RequestParam Long sellerId);

    @GetMapping("/api/inventories/count/products")
    long getProductCount(
            @RequestParam Long sellerId,
            @RequestParam String sourceRole
    );
}
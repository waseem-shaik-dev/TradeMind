package com.trademind.inventory.controller;

import com.trademind.inventory.dto.*;
import com.trademind.inventory.enums.OwnerType;
import com.trademind.inventory.service.InventoryAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryAnalyticsController {

    private final InventoryAnalyticsService service;

    @GetMapping("/low-stock")
    public List<LowStockResponse> getLowStock(@RequestParam Long sellerId) {
        return service.getLowStockProducts(sellerId);
    }

    @GetMapping("/count/low-stock")
    public int getLowStockCount(@RequestParam Long sellerId) {
        return service.getLowStockCount(sellerId);
    }

    @GetMapping("/count/out-of-stock")
    public int getOutOfStockCount(@RequestParam Long sellerId) {
        return service.getOutOfStockCount(sellerId);
    }

    @GetMapping("/summary")
    public InventorySummaryDto getSummary(@RequestParam Long sellerId) {
        return service.getSummary(sellerId);
    }

    @GetMapping("/count/products")
    public long getProductCount(
            @RequestParam Long sellerId,
            @RequestParam OwnerType sourceRole) {

        return service.getProductCount(sellerId, sourceRole);
    }
}
package com.trademind.inventory.controller;

import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.entity.StockItem;
import com.trademind.inventory.enums.MovementType;
import com.trademind.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public Inventory createInventory(
            @RequestParam Long ownerId,
            @RequestParam String location) {
        return inventoryService.createInventory(ownerId, location);
    }

    @PostMapping("/{inventoryId}/stock")
    public StockItem updateStock(
            @PathVariable Long inventoryId,
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam Integer reorderLevel,
            @RequestParam String referenceId,
            @RequestParam MovementType type) {

        return inventoryService.addOrUpdateStock(
                inventoryId,
                productId,
                quantity,
                reorderLevel,
                referenceId,
                type
        );
    }

    @GetMapping("/{inventoryId}/stock")
    public List<StockItem> getStock(@PathVariable Long inventoryId) {
        return inventoryService.getInventoryStock(inventoryId);
    }
}

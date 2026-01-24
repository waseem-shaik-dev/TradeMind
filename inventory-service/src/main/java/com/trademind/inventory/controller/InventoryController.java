package com.trademind.inventory.controller;

import com.trademind.inventory.dto.CatalogueInventoryResponse;
import com.trademind.inventory.dto.CreateInventoryRequest;
import com.trademind.inventory.dto.InventoryStockResponse;
import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.entity.StockItem;
import com.trademind.inventory.enums.MovementType;
import com.trademind.inventory.enums.OwnerType;
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
            @RequestBody CreateInventoryRequest request) {

        return inventoryService.createInventory(
                request.ownerId(),
                request.ownerType(),
                request.location(),
                request.primaryImageUrl()
        );
    }


    @GetMapping("/owner/{ownerId}")
    public List<InventoryStockResponse> getInventoryByOwner(
            @PathVariable Long ownerId,
            @RequestParam OwnerType ownerType) {

        return inventoryService.getInventoriesByOwner(ownerId, ownerType);
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

    @GetMapping("/{inventoryId}/stock-details")
    public List<InventoryStockResponse> getInventoryStockDetails(
            @PathVariable Long inventoryId) {
        return inventoryService.getInventoryWithStock(inventoryId);
    }

    @DeleteMapping("/stock/{stockItemId}")
    public void deleteStockItem(@PathVariable Long stockItemId) {
        inventoryService.deleteStockItem(stockItemId);
    }

    @GetMapping("/catalogue")
    public List<CatalogueInventoryResponse> getCatalogueInventory(
            @RequestParam OwnerType ownerType) {
        return inventoryService.getInventoryForCatalogue(ownerType);
    }

    @GetMapping("/catalogue/{productId}")
    public CatalogueInventoryResponse getCatalogueInventoryByProduct(
            @PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }


}

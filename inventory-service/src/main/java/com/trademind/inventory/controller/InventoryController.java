package com.trademind.inventory.controller;

import com.trademind.inventory.dto.*;
import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.enums.MovementType;
import com.trademind.inventory.enums.OwnerType;
import com.trademind.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;



    @GetMapping("/owner/{ownerId}")
    public List<InventoryStockResponse> getInventoryByOwner(
            @PathVariable Long ownerId,
            @RequestParam OwnerType ownerType) {

        return inventoryService.getInventoriesByOwner(ownerId, ownerType);
    }



    @PostMapping("/{inventoryId}/stock")
    public Inventory updateStock(
            @PathVariable Long inventoryId,
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam Integer reorderLevel,
            @RequestParam String referenceId,
            @RequestParam MovementType type,
            @RequestHeader("x-user-id") Long sellerId,
            @RequestHeader("x-user-role") OwnerType sellerRole
            ) {

        return inventoryService.addOrUpdateStock(
                sellerId,
                sellerRole,
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

    @GetMapping("/catalogue/seller/{sellerId}/product/{productId}")
    public CatalogueInventoryResponse getCatalogueInventoryByProduct(
            @PathVariable Long sellerId,
            @PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId,sellerId);
    }

    // INTERNAL – Cart validation
    @GetMapping("/internal/available")
    public Integer getAvailableQuantity(
            @RequestParam Long productId,
            @RequestParam Long sellerId,
            @RequestParam String sellerRole) {
        return inventoryService.getAvailableQuantity(productId,sellerId,sellerRole);
    }

    @GetMapping("/internal/product/{productId}/seller/{sellerId}/validate")
    public boolean validateStock(
            @PathVariable Long sellerId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        return inventoryService.hasSufficientStock(productId,sellerId, quantity);
    }

    // INTERNAL – Batch inventory fetch for cart
    @PostMapping("/internal/seller/{sellerId}/availability")
    public List<InventoryAvailabilityResponse> getAvailabilityForProducts(
            @PathVariable Long sellerId,
            @RequestBody List<Long> productIds) {

        return inventoryService.getAvailabilityForProducts(sellerId,productIds);
    }

    @PostMapping("/cancel/{checkoutId}")
    public ResponseEntity<String> cancelOrderStock(
            @PathVariable Long checkoutId
    ) {
        inventoryService.cancelCommittedStock(checkoutId);
        return ResponseEntity.ok("Stock restored after order cancellation");
    }


    @PostMapping("/bulk-upload")
    public void bulkUpload(
            @RequestHeader("X-USER-id") Long sourceId,
            @RequestHeader("X-USER-ROLE") String role,
            @RequestBody List<BulkInventoryRequest> items){

        inventoryService.bulkUpload(sourceId, OwnerType.valueOf(role), items);
    }

    @GetMapping("/seller/{sourceId}")
    public List<SellerInventoryViewResponse> getInventoryForSeller(
            @PathVariable Long sourceId){

        return inventoryService.getInventoryForSeller(sourceId);
    }


}

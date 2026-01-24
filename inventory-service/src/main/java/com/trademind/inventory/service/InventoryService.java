package com.trademind.inventory.service;

import com.trademind.inventory.dto.CatalogueInventoryResponse;
import com.trademind.inventory.dto.InventoryStockResponse;
import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.entity.StockItem;
import com.trademind.inventory.enums.MovementType;
import com.trademind.inventory.enums.OwnerType;

import java.util.List;

public interface InventoryService {

    Inventory createInventory(
            Long ownerId,
            OwnerType ownerType,
            String location,
            String primaryImageUrl
    );

    StockItem addOrUpdateStock(
            Long inventoryId,
            Long productId,
            Integer quantity,
            Integer reorderLevel,
            String referenceId,
            MovementType type
    );

    List<InventoryStockResponse> getInventoriesByOwner(
            Long ownerId,
            OwnerType ownerType
    );

    List<InventoryStockResponse> getInventoryWithStock(Long inventoryId);

    void deleteStockItem(Long stockItemId);

    List<CatalogueInventoryResponse> getInventoryForCatalogue(OwnerType ownerType);

    CatalogueInventoryResponse getInventoryByProductId(Long productId);

}

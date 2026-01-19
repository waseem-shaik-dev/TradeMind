package com.trademind.inventory.service;

import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.entity.StockItem;
import com.trademind.inventory.enums.MovementType;

import java.util.List;

public interface InventoryService {

    Inventory createInventory(Long ownerId, String location);

    StockItem addOrUpdateStock(
            Long inventoryId,
            Long productId,
            Integer quantity,
            Integer reorderLevel,
            String referenceId,
            MovementType type
    );

    List<StockItem> getInventoryStock(Long inventoryId);
}

package com.trademind.inventory.service;

import com.trademind.events.checkout.common.ItemQuantityDto;
import com.trademind.inventory.dto.*;
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

    Integer getAvailableQuantity(Long productId);

    boolean hasSufficientStock(Long productId, Integer requestedQty);

    List<InventoryAvailabilityResponse> getAvailabilityForProducts(
            List<Long> productIds
    );


    void reserveStock(Long checkoutId, List<ItemQuantityDto> items);

    // Payment service
    void finalizeReservedStock(Long checkoutId);

    // Checkout / Payment failure
    void releaseReservedStock(Long checkoutId);

    void cancelCommittedStock(Long checkoutId);

    void bulkUpload(
            Long sourceId,
            OwnerType role,
            List<BulkInventoryRequest> items
    );

    List<SellerInventoryViewResponse> getInventoryForSeller(Long sourceId);
}

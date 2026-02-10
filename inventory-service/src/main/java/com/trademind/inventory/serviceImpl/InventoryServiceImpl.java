package com.trademind.inventory.serviceImpl;

import com.trademind.events.checkout.common.ItemQuantityDto;
import com.trademind.inventory.dto.CatalogueInventoryResponse;
import com.trademind.inventory.dto.InventoryAvailabilityResponse;
import com.trademind.inventory.dto.InventoryStockResponse;
import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.entity.LowStockAlert;
import com.trademind.inventory.entity.StockItem;
import com.trademind.inventory.entity.StockMovement;
import com.trademind.inventory.enums.MovementType;
import com.trademind.inventory.enums.OwnerType;
import com.trademind.inventory.kafka.InventoryEventProducer;
import com.trademind.inventory.repository.InventoryRepository;
import com.trademind.inventory.repository.LowStockAlertRepository;
import com.trademind.inventory.repository.StockItemRepository;
import com.trademind.inventory.repository.StockMovementRepository;
import com.trademind.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final StockItemRepository stockRepo;
    private final StockMovementRepository movementRepo;
    private final LowStockAlertRepository alertRepo;
    private final InventoryEventProducer producer;

    @Override
    public Inventory createInventory(
            Long ownerId,
            OwnerType ownerType,
            String location,
            String primaryImageUrl) {

        String finalImageUrl =
                (primaryImageUrl == null || primaryImageUrl.isBlank())
                        ? null
                        : primaryImageUrl;

        return inventoryRepo.save(
                Inventory.builder()
                        .ownerId(ownerId)
                        .ownerType(ownerType)
                        .location(location)
                        .primaryImageUrl(finalImageUrl)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public List<CatalogueInventoryResponse> getInventoryForCatalogue(
            OwnerType ownerType) {

        return inventoryRepo.findByOwnerType(ownerType)
                .stream()
                .flatMap(inventory ->
                        stockRepo.findByInventoryId(inventory.getId())
                                .stream()
                                .map(stock -> new CatalogueInventoryResponse(
                                        stock.getProductId(),
                                        stock.getQuantityAvailable(),
                                        stock.getReservedQuantity(),
                                        stock.isOutOfStock()
                                ))
                )
                .toList();
    }

    @Override
    public CatalogueInventoryResponse getInventoryByProductId(Long productId) {

        StockItem stock = stockRepo.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        return new CatalogueInventoryResponse(
                stock.getProductId(),
                stock.getQuantityAvailable(),
                stock.getReservedQuantity(),
                stock.isOutOfStock()
        );
    }


    @Override
    public List<InventoryStockResponse> getInventoriesByOwner(
            Long ownerId,
            OwnerType ownerType) {

        List<Inventory> inventories =
                inventoryRepo.findByOwnerIdAndOwnerType(ownerId, ownerType);

        return inventories.stream()
                .flatMap(inventory ->
                        stockRepo.findByInventoryId(inventory.getId())
                                .stream()
                                .map(stock -> new InventoryStockResponse(
                                        inventory.getId(),
                                        inventory.getOwnerId(),
                                        inventory.getLocation(),
                                        inventory.getPrimaryImageUrl(),
                                        stock.getId(),
                                        stock.getProductId(),
                                        stock.getQuantityAvailable(),
                                        stock.getReservedQuantity(),
                                        stock.isOutOfStock(),
                                        stock.getReorderLevel(),

                                        inventory.getCreatedAt()
                                ))
                )
                .toList();
    }


    @Override
    public StockItem addOrUpdateStock(
            Long inventoryId,
            Long productId,
            Integer quantity,
            Integer reorderLevel,
            String referenceId,
            MovementType type) {

        StockItem stock = stockRepo
                .findByInventoryIdAndProductId(inventoryId, productId)
                .orElseGet(() -> {
                    StockItem s = new StockItem();
                    s.setInventoryId(inventoryId);
                    s.setProductId(productId);
                    s.setQuantityAvailable(0);
                    s.setReservedQuantity(0);
                    return s;
                });

        int updatedQty = switch (type) {
            case IN -> stock.getQuantityAvailable() + quantity;
            case OUT -> stock.getQuantityAvailable() - quantity;
            case ADJUSTMENT -> quantity;
        };

        stock.setQuantityAvailable(updatedQty);
        stock.setReorderLevel(reorderLevel);
        stock.setOutOfStock(updatedQty <= 0);

        StockItem saved = stockRepo.save(stock);

        movementRepo.save(
                new StockMovement(
                        null,
                        saved.getId(),
                        type,
                        quantity,
                        referenceId,
                        LocalDateTime.now()
                )
        );

        checkLowStock(saved);
      //  producer.publishStockUpdated(saved);

        return saved;
    }

    private void checkLowStock(StockItem stock) {
        if (stock.getQuantityAvailable() <= stock.getReorderLevel()) {
            alertRepo.findByStockItemIdAndResolvedFalse(stock.getId())
                    .orElseGet(() -> alertRepo.save(
                            new LowStockAlert(
                                    null,
                                    stock.getId(),
                                    LocalDateTime.now(),
                                    false
                            )
                    ));
        }
    }

    @Override
    public List<InventoryStockResponse> getInventoryWithStock(Long inventoryId) {

        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        return stockRepo.findByInventoryId(inventoryId)
                .stream()
                .map(stock -> new InventoryStockResponse(
                        inventory.getId(),
                        inventory.getOwnerId(),
                        inventory.getLocation(),
                        inventory.getPrimaryImageUrl(),
                        stock.getId(),
                        stock.getProductId(),
                        stock.getQuantityAvailable(),
                        stock.getReservedQuantity(),
                        stock.isOutOfStock(),
                        stock.getReorderLevel(),
                        inventory.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public void deleteStockItem(Long stockItemId) {

        StockItem stock = stockRepo.findById(stockItemId)
                .orElseThrow(() -> new RuntimeException("Stock item not found"));

        Long inventoryId = stock.getInventoryId();
        stockRepo.delete(stock);

        // delete inventory if no stock remains
        if (!stockRepo.existsByInventoryId(inventoryId)) {
            inventoryRepo.deleteById(inventoryId);
        }
    }

    @Override
    public Integer getAvailableQuantity(Long productId) {

        StockItem stock = stockRepo.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        return stock.getQuantityAvailable();
    }

    @Override
    public boolean hasSufficientStock(Long productId, Integer requestedQty) {

        StockItem stock = stockRepo.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        return stock.getQuantityAvailable() >= requestedQty;
    }

    @Override
    public List<InventoryAvailabilityResponse> getAvailabilityForProducts(
            List<Long> productIds) {

        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        return stockRepo.findByProductIdIn(productIds)
                .stream()
                .map(stock -> new InventoryAvailabilityResponse(
                        stock.getProductId(),
                        stock.getQuantityAvailable(),
                        stock.isOutOfStock()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void reserveStock(
            Long checkoutId,
            List<ItemQuantityDto> items
    ) {
        for (ItemQuantityDto item : items) {

            StockItem stock = stockRepo
                    .findByProductIdForUpdate(item.productId())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Stock not found for product " + item.productId()
                            )
                    );

            if (stock.getQuantityAvailable() < item.quantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for product " + item.productId()
                );
            }

            stock.setQuantityAvailable(
                    stock.getQuantityAvailable() - item.quantity()
            );

            stock.setReservedQuantity(
                    stock.getReservedQuantity() + item.quantity()
            );

            // maintain invariants
            stock.setOutOfStock(stock.getQuantityAvailable() == 0);
        }
    }

    // ---------------------------------------------------------

    @Override
    @Transactional
    public void releaseStock(
            Long checkoutId,
            List<ItemQuantityDto> items
    ) {
        for (ItemQuantityDto item : items) {

            stockRepo
                    .findByProductIdForUpdate(item.productId())
                    .ifPresent(stock -> {

                        int releasedQty =
                                Math.min(item.quantity(), stock.getReservedQuantity());

                        stock.setReservedQuantity(
                                stock.getReservedQuantity() - releasedQty
                        );

                        stock.setQuantityAvailable(
                                stock.getQuantityAvailable() + releasedQty
                        );

                        stock.setOutOfStock(false);
                    });
        }
    }

}

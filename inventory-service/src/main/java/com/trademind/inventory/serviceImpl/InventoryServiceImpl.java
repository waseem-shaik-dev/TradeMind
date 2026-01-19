package com.trademind.inventory.serviceImpl;

import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.entity.LowStockAlert;
import com.trademind.inventory.entity.StockItem;
import com.trademind.inventory.entity.StockMovement;
import com.trademind.inventory.enums.MovementType;
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
    public Inventory createInventory(Long ownerId, String location) {
        return inventoryRepo.save(
                Inventory.builder()
                        .ownerId(ownerId)
                        .location(location)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
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
                    return s;
                });

        int updatedQty = type == MovementType.OUT
                ? stock.getQuantityAvailable() - quantity
                : stock.getQuantityAvailable() + quantity;

        stock.setQuantityAvailable(updatedQty);
        stock.setReorderLevel(reorderLevel);

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
        producer.publishStockUpdated(saved);

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
    public List<StockItem> getInventoryStock(Long inventoryId) {
        return stockRepo.findAll()
                .stream()
                .filter(s -> s.getInventoryId().equals(inventoryId))
                .toList();
    }
}

package com.trademind.inventory.serviceImpl;

import com.trademind.events.checkout.common.ItemQuantityDto;
import com.trademind.inventory.dto.*;
import com.trademind.inventory.entity.*;
import com.trademind.inventory.enums.MovementType;
import com.trademind.inventory.enums.OwnerType;
import com.trademind.inventory.enums.ReservationStatus;
import com.trademind.inventory.kafka.InventoryEventProducer;
import com.trademind.inventory.repository.*;
import com.trademind.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final StockItemRepository stockRepo;
    private final StockMovementRepository movementRepo;
    private final LowStockAlertRepository alertRepo;
    private final InventoryEventProducer producer;
    private final StockReservationRepository reservationRepo;

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

    // --------------------------------------------------
    // 1️⃣ RESERVE (Checkout.confirm)
    // --------------------------------------------------
    @Override
    public void reserveStock(
            Long checkoutId,
            List<ItemQuantityDto> items
    ) {
        try {


            for (ItemQuantityDto item : items) {

                if (reservationRepo.existsByCheckoutIdAndProductId(
                        checkoutId, item.productId()
                )) {
                    continue; // idempotent
                }

                StockItem stock = stockRepo
                        .findByProductIdForUpdate(item.productId())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Stock not found for product "
                                                + item.productId()
                                )
                        );

                if (stock.getQuantityAvailable() < item.quantity()) {
                    throw new IllegalStateException(
                            "Insufficient stock for product "
                                    + item.productId()
                    );
                }

                stock.setQuantityAvailable(
                        stock.getQuantityAvailable() - item.quantity()
                );
                stock.setOutOfStock(
                        stock.getQuantityAvailable() <= 0
                );

                StockReservation r = new StockReservation();
                r.setCheckoutId(checkoutId);
                r.setProductId(item.productId());
                r.setQuantity(item.quantity());
                r.setStatus(ReservationStatus.RESERVED);

                reservationRepo.save(r);
            }

        }

        catch (IllegalStateException ex) {

            // 🔥 Fire failure event BEFORE rollback completes
            producer.publishInventoryReserveFailed(
                    checkoutId,
                    ex.getMessage()
            );

            // 🔁 rollback DB transaction
            throw ex;
        }

    }

    // --------------------------------------------------
    // 2️⃣ FINALIZE (Payment.success)
    // --------------------------------------------------
    @Override
    public void finalizeReservedStock(Long checkoutId) {

        List<StockReservation> reservations =
                reservationRepo.findAllByCheckoutId(checkoutId);

        if (reservations.isEmpty()) {
            return; // idempotent
        }

        for (StockReservation r : reservations) {
            r.setStatus(ReservationStatus.COMMITTED);
        }

        reservationRepo.saveAll(reservations);
    }


    // ---------------------------------------------------------

    // --------------------------------------------------
    // 3️⃣ RELEASE (Payment.failed / Checkout.expired)
    // --------------------------------------------------
    @Override
    public void releaseReservedStock(Long checkoutId) {

        List<StockReservation> reservations =
                reservationRepo.findAllByCheckoutId(checkoutId);

        for (StockReservation r : reservations) {

            if (r.getStatus() != ReservationStatus.RESERVED) {
                continue; // do not release committed
            }


            StockItem stock = stockRepo
                    .findByProductIdForUpdate(r.getProductId())
                    .orElseThrow();

            stock.setQuantityAvailable(
                    stock.getQuantityAvailable() + r.getQuantity()
            );
            stock.setOutOfStock(false);
            r.setStatus(ReservationStatus.RELEASED);
        }

        // 🔥 cleanup
        reservationRepo.deleteAllByCheckoutId(checkoutId);


    }

    @Override
    public void cancelCommittedStock(Long checkoutId) {

        List<StockReservation> reservations =
                reservationRepo.findAllByCheckoutId(checkoutId);

        for (StockReservation r : reservations) {

            if (r.getStatus() != ReservationStatus.COMMITTED) {
                continue; // only restore committed
            }

            StockItem stock = stockRepo
                    .findByProductIdForUpdate(r.getProductId())
                    .orElseThrow();

            stock.setQuantityAvailable(
                    stock.getQuantityAvailable() + r.getQuantity()
            );

            stock.setOutOfStock(
                    stock.getQuantityAvailable() <= 0
            );

            r.setStatus(ReservationStatus.CANCELLED);

            movementRepo.save(
                    new StockMovement(
                            null,
                            stock.getId(),
                            MovementType.IN,
                            r.getQuantity(),
                            "ORDER_CANCEL_" + checkoutId,
                            LocalDateTime.now()
                    )
            );
        }

        reservationRepo.deleteAllByCheckoutId(checkoutId);
    }

    @Override
    @Transactional
    public void bulkUpload(
            Long sourceId,
            OwnerType role,
            List<BulkInventoryRequest> items) {

        if(items == null || items.isEmpty()){
            return;
        }

        // 1️⃣ Find or create seller inventory
        Inventory inventory =
                inventoryRepo
                        .findByOwnerIdAndOwnerType(sourceId, role)
                        .orElseGet(() ->
                                inventoryRepo.save(
                                        Inventory.builder()
                                                .ownerId(sourceId)
                                                .ownerType(role)
                                                .createdAt(LocalDateTime.now())
                                                .build()
                                )
                        );

        Long inventoryId = inventory.getId();


        // 2️⃣ Process each product
        for(BulkInventoryRequest item : items){

            Optional<StockItem> existing =
                    stockRepo.findByInventoryIdAndProductId(
                            inventoryId,
                            item.productid()
                    );

            if(existing.isPresent()){

                StockItem stock = existing.get();

                stock.setQuantityAvailable(
                        stock.getQuantityAvailable() + item.quantity()
                );

                stock.setPrice(item.price());

                stockRepo.save(stock);

            }
            else{

                StockItem stock =
                        StockItem.builder()
                                .inventoryId(inventoryId)
                                .productId(item.productid())
                                .quantityAvailable(item.quantity())
                                .price(item.price())
                                .outOfStock(item.quantity() <= 0)
                                .sourceId(sourceId)
                                .sourceRole(role)
                                .build();

                stockRepo.save(stock);
            }
        }
    }

    public List<SellerInventoryViewResponse> getInventoryForSeller(Long sourceId){

        return stockRepo
                .findBySourceId(sourceId)
                .stream()
                .map(inv -> new SellerInventoryViewResponse(
                        inv.getId(),
                        inv.getProductId(),
                        inv.getSourceId(),
                        inv.getSourceRole().name(),
                        inv.getQuantityAvailable(),
                        inv.getPrice()
                ))
                .toList();
    }


}

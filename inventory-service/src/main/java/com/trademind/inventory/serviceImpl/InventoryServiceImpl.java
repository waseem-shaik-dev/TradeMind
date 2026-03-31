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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final StockMovementRepository movementRepo;
    private final LowStockAlertRepository alertRepo;
    private final InventoryEventProducer producer;
    private final StockReservationRepository reservationRepo;



    @Override
    public List<CatalogueInventoryResponse> getInventoryForCatalogue(
            OwnerType ownerType) {

        return inventoryRepo.findBySellerRole(ownerType)
                .stream()
                .map(inv -> new CatalogueInventoryResponse(
                        inv.getProductId(),
                        inv.getSellerId(),
                        inv.getSellerRole().name(),
                        inv.getQuantityAvailable(),
                        inv.isOutOfStock()
                ))
                .toList();
    }

    @Override
    public CatalogueInventoryResponse getInventoryByProductId(Long productId,Long sellerId) {

        Inventory inv = inventoryRepo.findByProductIdAndSellerId(productId,sellerId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        return new CatalogueInventoryResponse(
                inv.getProductId(),
                inv.getSellerId(),
                inv.getSellerRole().name(),
                inv.getQuantityAvailable(),
                inv.isOutOfStock()
        );
    }


    @Override
    public List<InventoryStockResponse> getInventoriesByOwner(
            Long sellerId,
            OwnerType role) {

        return inventoryRepo.findBySellerId(sellerId)
                .stream()
                .map(inv -> new InventoryStockResponse(
                        inv.getId(),
                        inv.getSellerId(),
                        inv.getSellerRole().name(),
                        inv.getProductId(),
                        inv.getQuantityAvailable(),
                        inv.getPrice(),
                        inv.getProductName(),
                        inv.isOutOfStock(),
                        inv.getReorderLevel(),
                        inv.getPrimaryImageUrl(),
                        inv.getCreatedAt()
                ))
                .toList();
    }


    @Override
    public Inventory addOrUpdateStock(
            Long sellerId,
            OwnerType role,
            Long productId,
            Integer quantity,
            Integer reorderLevel,
            String referenceId,
            MovementType type) {

        Inventory inv = inventoryRepo
                .findByProductIdAndSellerIdAndSellerRole(
                        productId,
                        sellerId,
                        role
                )
                .orElseGet(() -> Inventory.builder()
                        .productId(productId)
                        .sellerId(sellerId)
                        .sellerRole(role)
                        .quantityAvailable(0)
                        .build());

        int updatedQty = switch (type) {
            case IN -> inv.getQuantityAvailable() + quantity;
            case OUT -> inv.getQuantityAvailable() - quantity;
            case ADJUSTMENT -> quantity;
        };

        inv.setQuantityAvailable(updatedQty);
        inv.setReorderLevel(reorderLevel);
        inv.setOutOfStock(updatedQty <= 0);

        Inventory saved = inventoryRepo.save(inv);

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

        return saved;
    }

    private void checkLowStock(Inventory inv) {

        if (inv.getQuantityAvailable() <= inv.getReorderLevel()) {

            alertRepo.findByStockItemIdAndResolvedFalse(inv.getId())
                    .orElseGet(() ->
                            alertRepo.save(
                                    new LowStockAlert(
                                            null,
                                            inv.getId(),
                                            LocalDateTime.now(),
                                            false
                                    )
                            ));
        }
    }

    @Override
    public List<InventoryStockResponse> getInventoryWithStock(Long sellerId) {

        return inventoryRepo.findBySellerId(sellerId)
                .stream()
                .map(inv -> new InventoryStockResponse(
                        inv.getId(),
                        inv.getSellerId(),
                        inv.getSellerRole().name(),
                        inv.getProductId(),
                        inv.getQuantityAvailable(),
                        inv.getPrice(),
                        inv.getProductName(),
                        inv.isOutOfStock(),
                        inv.getReorderLevel(),
                        inv.getPrimaryImageUrl(),
                        inv.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public void deleteStockItem(Long inventoryId) {

        Inventory inv = inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventoryRepo.delete(inv);
    }

    @Override
    public Integer getAvailableQuantity(Long productId, Long sellerId, String sellerRole) {

        System.out.print("checking stock for productId: "+productId+"   sellerId: "+sellerId+"    sellerRole: "+sellerRole);

        Inventory inv = inventoryRepo.findByProductIdAndSellerIdAndSellerRole(productId,sellerId,OwnerType.valueOf(sellerRole))
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        return inv.getQuantityAvailable();
    }

    @Override
    public boolean hasSufficientStock(Long productId,Long sourceId,Integer requestedQty) {

        Inventory inv = inventoryRepo.findByProductIdAndSellerId(
                        productId,
                        sourceId
                )
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        return inv.getQuantityAvailable() >= requestedQty;
    }

    @Override
    public List<InventoryAvailabilityResponse> getAvailabilityForProducts(
            Long sellerId,
            List<Long> productIds) {

        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        return inventoryRepo.findAvailabilityByProductsAndSeller(
                productIds,
                sellerId
        );
    }

    // --------------------------------------------------
    // 1️⃣ RESERVE (Checkout.confirm)
    // --------------------------------------------------
    @Override
    public void reserveStock(Long checkoutId,Long sourceId,
                             String sourceRole, List<ItemQuantityDto> items) {

        try {

            for (ItemQuantityDto item : items) {

                if (reservationRepo.existsByCheckoutIdAndProductId(
                        checkoutId, item.productId())) {
                    continue;
                }

                Inventory inv = inventoryRepo
                        .findByProductIdAndSellerIdAndSellerRole(
                                item.productId(),
                                sourceId,
                                OwnerType.valueOf(sourceRole)
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Stock not found for product "
                                                + item.productId()
                                )
                        );

                if (inv.getQuantityAvailable() < item.quantity()) {
                    throw new IllegalStateException(
                            "Insufficient stock for product "
                                    + item.productId()
                    );
                }

                inv.setQuantityAvailable(
                        inv.getQuantityAvailable() - item.quantity()
                );

                inv.setOutOfStock(
                        inv.getQuantityAvailable() <= 0
                );

                inventoryRepo.save(inv);

                StockReservation r = new StockReservation();
                r.setCheckoutId(checkoutId);
                r.setProductId(item.productId());
                r.setSellerId(sourceId);
                r.setSellerRole(OwnerType.valueOf(sourceRole));
                r.setQuantity(item.quantity());
                r.setStatus(ReservationStatus.RESERVED);

                reservationRepo.save(r);
            }

        } catch (IllegalStateException ex) {

            producer.publishInventoryReserveFailed(
                    checkoutId,
                    ex.getMessage()
            );

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
                continue;
            }

            Inventory inv = inventoryRepo
                    .findByProductIdAndSellerIdAndSellerRole(
                            r.getProductId(),
                            r.getSellerId(),
                            r.getSellerRole()
                    )
                    .orElseThrow();

            inv.setQuantityAvailable(
                    inv.getQuantityAvailable() + r.getQuantity()
            );

            inv.setOutOfStock(false);

            inventoryRepo.save(inv);

            r.setStatus(ReservationStatus.RELEASED);
        }

        reservationRepo.deleteAllByCheckoutId(checkoutId);
    }

    @Override
    public void cancelCommittedStock(Long checkoutId) {

        List<StockReservation> reservations =
                reservationRepo.findAllByCheckoutId(checkoutId);

        for (StockReservation r : reservations) {

            if (r.getStatus() != ReservationStatus.COMMITTED) {
                continue;
            }

            Inventory inv = inventoryRepo
                    .findByProductIdAndSellerIdAndSellerRole(
                            r.getProductId(),
                            r.getSellerId(),
                            r.getSellerRole()
                    )
                    .orElseThrow();

            inv.setQuantityAvailable(
                    inv.getQuantityAvailable() + r.getQuantity()
            );

            inv.setOutOfStock(
                    inv.getQuantityAvailable() <= 0
            );

            inventoryRepo.save(inv);

            r.setStatus(ReservationStatus.CANCELLED);

            movementRepo.save(
                    new StockMovement(
                            null,
                            inv.getId(),
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
            Long sellerId,
            OwnerType role,
            List<BulkInventoryRequest> items) {

        if (items == null || items.isEmpty()) {
            return;
        }

        // 1️⃣ Extract productIds
        List<Long> productIds =
                items.stream()
                        .map(BulkInventoryRequest::productId)
                        .toList();


        // 2️⃣ Fetch existing inventory in ONE query
        List<Inventory> existingInventories =
                inventoryRepo
                        .findByProductIdInAndSellerIdAndSellerRole(
                                productIds,
                                sellerId,
                                role
                        );


        // 3️⃣ Convert to Map for O(1) lookup
        Map<Long, Inventory> inventoryMap =
                existingInventories.stream()
                        .collect(Collectors.toMap(
                                Inventory::getProductId,
                                inv -> inv
                        ));


        List<Inventory> toSave = new ArrayList<>();


        // 4️⃣ Process items
        for (BulkInventoryRequest item : items) {

            Inventory inv = inventoryMap.get(item.productId());

            if (inv != null) {

                inv.setQuantityAvailable(
                        inv.getQuantityAvailable() + item.quantity()
                );

                inv.setProductName(item.productName());

                inv.setPrice(item.price());

                inv.setOutOfStock(
                        inv.getQuantityAvailable() <= 0
                );

                inv.setPrimaryImageUrl(item.primaryImageUrl());

                toSave.add(inv);

            } else {

                Inventory newInv =
                        Inventory.builder()
                                .productId(item.productId())
                                .sellerId(sellerId)
                                .sellerRole(role)
                                .quantityAvailable(item.quantity())
                                .productName(item.productName())
                                .price(item.price())
                                .primaryImageUrl(item.primaryImageUrl())
                                .outOfStock(item.quantity() <= 0)
                                .build();

                toSave.add(newInv);
            }
        }

        // 5️⃣ Batch save
        inventoryRepo.saveAll(toSave);
    }

    @Override
    public List<SellerInventoryViewResponse> getInventoryForSeller(Long sellerId){

        List<SellerInventoryViewResponse> res = inventoryRepo
                .findBySellerId(sellerId)
                .stream()
                .map(inv -> new SellerInventoryViewResponse(
                        inv.getId(),
                        inv.getProductId(),
                        inv.getSellerId(),
                        inv.getSellerRole().name(),
                        inv.getProductName(),
                        inv.getQuantityAvailable(),
                        inv.getPrice(),
                        inv.getPrimaryImageUrl()
                ))
                .toList();

        System.out.println("inventory fetching : \n\n\n"+res);

        return  res;
    }


}

package com.trademind.inventory.repository;

import com.trademind.inventory.dto.InventoryAvailabilityResponse;
import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductIdAndSellerIdAndSellerRole(
            Long productId,
            Long sellerId,
            OwnerType sellerRole
    );

    List<Inventory> findBySellerId(Long sellerId);

    List<Inventory> findBySellerRole(OwnerType role);

    Optional<Inventory> findByProductId(Long productId);

    List<Inventory> findByProductIdIn(List<Long> productIds);

    List<Inventory> findByProductIdInAndSellerIdAndSellerRole(
            List<Long> productIds,
            Long sellerId,
            OwnerType role
    );

    @Query("""
    SELECT new com.trademind.inventory.dto.InventoryAvailabilityResponse(
        i.productId,
        i.quantityAvailable,
        i.outOfStock
    )
    FROM Inventory i
    WHERE i.productId IN :productIds
      AND i.sellerId = :sellerId
""")
    List<InventoryAvailabilityResponse> findAvailabilityByProductsAndSeller(
            List<Long> productIds,
            Long sellerId
    );
}
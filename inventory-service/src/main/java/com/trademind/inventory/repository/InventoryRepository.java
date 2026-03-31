package com.trademind.inventory.repository;

import com.trademind.inventory.dto.InventoryAvailabilityResponse;
import com.trademind.inventory.dto.InventorySummaryDto;
import com.trademind.inventory.dto.LowStockResponse;
import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

//    Optional<Inventory> findByProductId(Long productId);

    List<Inventory> findByProductIdIn(List<Long> productIds);

    List<Inventory> findByProductIdInAndSellerIdAndSellerRole(
            List<Long> productIds,
            Long sellerId,
            OwnerType role
    );

    Optional<Inventory> findByProductIdAndSellerId(Long productId,Long sellerId);

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


    // ================= LOW STOCK PRODUCTS =================
    @Query("""
        SELECT new com.trademind.inventory.dto.LowStockResponse(
            i.productName,
            '',
            i.quantityAvailable,
            i.reorderLevel
        )
        FROM Inventory i
        WHERE i.sellerId = :sellerId
        AND i.quantityAvailable <= i.reorderLevel
    """)
    List<LowStockResponse> getLowStockProducts(@Param("sellerId") Long sellerId);


    // ================= LOW STOCK COUNT =================
    @Query("""
        SELECT COUNT(i)
        FROM Inventory i
        WHERE i.sellerId = :sellerId
        AND i.quantityAvailable <= i.reorderLevel
    """)
    int getLowStockCount(@Param("sellerId") Long sellerId);


    // ================= OUT OF STOCK COUNT =================
    @Query("""
        SELECT COUNT(i)
        FROM Inventory i
        WHERE i.sellerId = :sellerId
        AND i.outOfStock = true
    """)
    int getOutOfStockCount(@Param("sellerId") Long sellerId);


    // ================= INVENTORY SUMMARY =================
    @Query("""
        SELECT new com.trademind.inventory.dto.InventorySummaryDto(
            COUNT(i),
            COALESCE(SUM(CASE WHEN i.quantityAvailable <= i.reorderLevel THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN i.outOfStock = true THEN 1 ELSE 0 END), 0),
            CAST(COALESCE(SUM(i.price * i.quantityAvailable), 0) as string)
        )
        FROM Inventory i
        WHERE i.sellerId = :sellerId
    """)
    InventorySummaryDto getInventorySummary(@Param("sellerId") Long sellerId);

    @Query("""
    SELECT COUNT(i)
    FROM Inventory i
    WHERE i.sellerId = :sellerId
    AND i.sellerRole = :sourceRole
""")
    long countProductsBySource(
            @Param("sellerId") Long sellerId,
            @Param("sourceRole") OwnerType sourceRole
    );

    @Query("""
    SELECT COUNT(i)
    FROM Inventory i
    WHERE i.sellerRole = :sourceRole
""")
    long countAllBySourceRole(@Param("sourceRole") OwnerType sourceRole);
}
package com.trademind.inventory.repository;

import com.trademind.inventory.entity.StockItem;
import com.trademind.inventory.enums.OwnerType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    Optional<StockItem> findByInventoryIdAndProductId(Long inventoryId, Long productId);

    List<StockItem> findByInventoryId(Long inventoryId);

    boolean existsByInventoryId(Long inventoryId);

    Optional<StockItem> findByProductId(Long productId);

    List<StockItem> findByProductIdIn(List<Long> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from StockItem s where s.productId = :productId")
    Optional<StockItem> findByProductIdForUpdate(Long productId);

    List<StockItem> findBySourceId(Long sourceId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<StockItem> findByProductIdAndSourceIdAndSourceRole(
            Long productId,
            Long sourceId,
            OwnerType sourceRole
    );


}

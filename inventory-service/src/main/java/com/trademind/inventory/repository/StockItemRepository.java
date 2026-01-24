package com.trademind.inventory.repository;

import com.trademind.inventory.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    Optional<StockItem> findByInventoryIdAndProductId(Long inventoryId, Long productId);

    List<StockItem> findByInventoryId(Long inventoryId);

    boolean existsByInventoryId(Long inventoryId);

    Optional<StockItem> findByProductId(Long productId);

}

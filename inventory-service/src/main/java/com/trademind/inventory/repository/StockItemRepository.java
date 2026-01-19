package com.trademind.inventory.repository;

import com.trademind.inventory.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    Optional<StockItem> findByInventoryIdAndProductId(Long inventoryId, Long productId);
}

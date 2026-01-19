package com.trademind.inventory.repository;

import com.trademind.inventory.entity.LowStockAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LowStockAlertRepository extends JpaRepository<LowStockAlert, Long> {
    Optional<LowStockAlert> findByStockItemIdAndResolvedFalse(Long stockItemId);
}

package com.trademind.inventory.repository;

import com.trademind.inventory.entity.LowStockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LowStockAlertRepository extends JpaRepository<LowStockAlert, Long> {
    Optional<LowStockAlert> findByStockItemIdAndResolvedFalse(Long stockItemId);
    @Query("""
    SELECT COUNT(a)
    FROM LowStockAlert a
    WHERE a.resolved = false
""")
    long getActiveAlerts();
}

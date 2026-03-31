package com.trademind.inventory.repository;

import com.trademind.inventory.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @Query("""
    SELECT COUNT(sm)
    FROM StockMovement sm
    WHERE sm.type = 'OUT'
    AND sm.timestamp >= :startDate
""")
    long getStockOutCount(LocalDateTime startDate);
}

package com.trademind.inventory.repository;

import com.trademind.inventory.entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockReservationRepository extends JpaRepository<StockReservation,Long> {
    boolean existsByCheckoutIdAndProductId(Long checkoutId, Long productId);

    List<StockReservation> findAllByCheckoutId(Long checkoutId);

    void deleteAllByCheckoutId(Long checkoutId);

    @Query("""
    SELECT COUNT(r)
    FROM StockReservation r
    WHERE r.status = 'RESERVED'
""")
    long getActiveReservations();
}

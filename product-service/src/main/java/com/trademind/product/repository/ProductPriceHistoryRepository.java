package com.trademind.product.repository;

import com.trademind.product.entity.ProductPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductPriceHistoryRepository
        extends JpaRepository<ProductPriceHistory, Long> {

    @Query("""
       SELECT p FROM ProductPriceHistory p
       WHERE p.productId = :productId
       AND p.effectiveFrom <= CURRENT_TIMESTAMP
       AND (p.effectiveTo IS NULL OR p.effectiveTo >= CURRENT_TIMESTAMP)
    """)
    Optional<ProductPriceHistory> findCurrentPrice(Long productId);
}

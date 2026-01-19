package com.trademind.analytics.repository;

import com.trademind.analytics.entity.TopSellingProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopSellingProductRepository
        extends JpaRepository<TopSellingProduct, Long> {}

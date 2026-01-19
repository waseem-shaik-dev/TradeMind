package com.trademind.product.repository;

import com.trademind.product.entity.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitOfMeasureRepository
        extends JpaRepository<UnitOfMeasure, Long> {}

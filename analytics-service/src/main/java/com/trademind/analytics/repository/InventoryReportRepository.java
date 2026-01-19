package com.trademind.analytics.repository;

import com.trademind.analytics.entity.InventoryReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryReportRepository
        extends JpaRepository<InventoryReport, Long> {}

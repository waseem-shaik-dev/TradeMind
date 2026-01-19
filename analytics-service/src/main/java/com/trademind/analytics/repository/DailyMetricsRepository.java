package com.trademind.analytics.repository;

import com.trademind.analytics.entity.DailyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyMetricsRepository
        extends JpaRepository<DailyMetrics, Long> {}

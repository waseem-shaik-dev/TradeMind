package com.trademind.analytics.repository;

import com.trademind.analytics.entity.ProfitLossReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfitLossReportRepository
        extends JpaRepository<ProfitLossReport, Long> {}

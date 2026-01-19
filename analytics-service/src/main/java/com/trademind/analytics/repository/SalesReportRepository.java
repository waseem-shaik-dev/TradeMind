package com.trademind.analytics.repository;

import com.trademind.analytics.entity.SalesReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SalesReportRepository
        extends JpaRepository<SalesReport, Long> {
    Optional<SalesReport> findByReportDate(LocalDate date);
}

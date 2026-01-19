package com.trademind.analytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "profit_loss_reports")
@Getter
@Setter
public class ProfitLossReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reportDate;

    private BigDecimal revenue;
    private BigDecimal expense;
    private BigDecimal profit;
}

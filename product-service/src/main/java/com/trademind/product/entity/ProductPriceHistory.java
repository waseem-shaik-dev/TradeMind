package com.trademind.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "product_price_history")
@Getter @Setter
public class ProductPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private BigDecimal price;

    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
}

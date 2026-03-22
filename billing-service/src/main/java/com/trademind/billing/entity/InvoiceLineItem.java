package com.trademind.billing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_line_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    private Long productId;

    private String productName;

    private String sku;

    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal totalPrice;
}
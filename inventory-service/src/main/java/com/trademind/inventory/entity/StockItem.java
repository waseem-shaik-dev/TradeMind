package com.trademind.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "stock_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"inventory_id", "product_id"})
)
@Getter
@Setter
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long inventoryId;
    private Long productId;

    private Integer quantityAvailable;

    @Column(nullable = false)
    private Integer reservedQuantity = 0;

    private Integer reorderLevel;

    @Column(nullable = false)
    private boolean outOfStock;
}

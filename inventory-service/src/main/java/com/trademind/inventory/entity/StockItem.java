package com.trademind.inventory.entity;

import com.trademind.inventory.enums.OwnerType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "stock_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"inventory_id", "product_id","source_id","source_role"})
)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long inventoryId;
    private Long productId;

    private Long sourceId;
    private OwnerType sourceRole;

    private Integer quantityAvailable;

    private BigDecimal price;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    private Integer reorderLevel;

    @Column(nullable = false)
    private boolean outOfStock;

    @PrePersist
    private void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}

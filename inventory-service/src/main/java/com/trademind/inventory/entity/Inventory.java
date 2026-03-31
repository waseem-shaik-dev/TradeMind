package com.trademind.inventory.entity;

import com.trademind.inventory.enums.OwnerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "inventories",
        indexes = {
                @Index(name = "idx_inventory_seller",columnList = "seller_id"),
                @Index(name = "idx_inventory_quantity",columnList = "quantity_available"),
                @Index(name = "idx_inventory_out_stock",columnList = "out_of_stock")
        },
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"product_id","seller_id","seller_role"}
        )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productId;

    private Long sellerId;

    @Enumerated(EnumType.STRING)
    private OwnerType sellerRole;

    private Integer quantityAvailable;

    private BigDecimal price;

    private Integer reorderLevel;

    private String productName;

    @Column(nullable = false)
    private boolean outOfStock;

    @Column(length = 1024)
    private String primaryImageUrl;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
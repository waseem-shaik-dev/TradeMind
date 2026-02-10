package com.trademind.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cart_product",
                        columnNames = {"cart_id", "product_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Owning cart
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * Product ID from Catalogue / Product Service
     */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /**
     * Quantity user wants
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Price snapshot (optional but recommended)
     * Used for showing cart summary even if price changes later
     */
    @Column(nullable = false)
    private BigDecimal unitPrice;

    /**
     * Optional: quick validation
     */
    private boolean available;
}

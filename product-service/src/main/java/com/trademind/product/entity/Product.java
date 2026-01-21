package com.trademind.product.entity;

import com.trademind.product.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_product_sku", columnList = "sku"),
                @Index(name = "idx_product_name", columnList = "name")
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(length = 1000)
    private String description;

    /* -------- Relationships (IDs only – microservice safe) -------- */

    private Long categoryId;
    private Long brandId;
    private Long unitOfMeasureId;

    /* -------- Business Flags -------- */

    private boolean returnable;
    private boolean taxable;

    /* -------- Image Handling -------- */

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductImage> images;
}

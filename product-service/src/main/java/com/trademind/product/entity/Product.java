package com.trademind.product.entity;

import com.trademind.product.entity.BaseEntity;
import com.trademind.product.enums.OwnerType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    private Long ownerId;

    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;

    /* -------- Business Flags -------- */

    private boolean returnable;
    private boolean taxable;

    /* -------- Image Handling -------- */

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);
    }

}

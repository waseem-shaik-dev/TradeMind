package com.trademind.product.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "product_categories")
@Getter @Setter
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long parentCategoryId;
}

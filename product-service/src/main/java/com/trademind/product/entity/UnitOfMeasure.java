package com.trademind.product.entity;

import com.trademind.product.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "units_of_measure")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UnitOfMeasure extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // kg, litre, piece
}

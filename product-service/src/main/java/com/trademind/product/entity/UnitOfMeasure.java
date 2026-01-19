package com.trademind.product.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "units_of_measure")
@Getter @Setter
public class UnitOfMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // kg, litre, piece
}

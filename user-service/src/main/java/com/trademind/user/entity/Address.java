package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "addresses")
@Getter @Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String line1;
    private String city;
    private String state;
    private String pincode;
    private String country;
}

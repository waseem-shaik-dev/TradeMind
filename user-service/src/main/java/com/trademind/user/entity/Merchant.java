package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "merchants")
@Getter @Setter
public class Merchant {

    @Id
    private Long userId;

    private String businessName;
    private String licenseNumber;
}

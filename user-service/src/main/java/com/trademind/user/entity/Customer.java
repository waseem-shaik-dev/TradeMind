package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "customers")
@Getter @Setter
public class Customer {

    @Id
    private Long userId;

    private Integer loyaltyPoints;
}

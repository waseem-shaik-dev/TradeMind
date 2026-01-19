package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "retailers")
@Getter @Setter
public class Retailer {

    @Id
    private Long userId;

    private String shopName;
    private String gstNumber;
}

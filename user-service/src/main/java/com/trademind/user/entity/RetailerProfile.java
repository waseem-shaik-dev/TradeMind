package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "retailer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetailerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;

    private String shopEmail;

    private String shopImageUrl;
    private String shopImagePublicId;

    @OneToOne
    @JoinColumn(name = "store_address_id")
    private StoreAddress storeAddress;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
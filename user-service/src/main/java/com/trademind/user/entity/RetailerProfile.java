package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "retailer_profiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetailerProfile {

    @Id
    private Long id;

    private String shopName;
    private String shopEmail;
    private String gstNumber;
    private Double latitude;
    private Double longitude;
    private String mapUrl;
    private Boolean verified;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}

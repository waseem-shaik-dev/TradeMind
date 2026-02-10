package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "merchant_profiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantProfile {

    @Id
    private Long id;

    private String businessName;
    private String businessEmail;
    private String gstNumber;
    private String licenseNumber;
    private Double latitude;
    private Double longitude;
    private String mapUrl;
    private Boolean verified;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}

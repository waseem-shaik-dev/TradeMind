package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_profiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfile {

    @Id
    private Long id;

    private Integer loyaltyPoints;
    private Boolean newsletterSubscribed;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}

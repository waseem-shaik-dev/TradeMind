package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_profiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProfile {

    @Id
    private Long id;

    private String department;
    private String designation;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}

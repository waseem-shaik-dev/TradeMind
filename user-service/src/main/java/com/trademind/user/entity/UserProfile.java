package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;
    private String email;
    private String avatarUrl;
    private String avatarPublicId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

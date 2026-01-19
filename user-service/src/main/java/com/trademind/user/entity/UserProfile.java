package com.trademind.user.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user_profiles")
@Getter @Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String fullName;
    private String phone;
    private String email;
}

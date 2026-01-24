package com.trademind.inventory.entity;

import com.trademind.inventory.enums.OwnerType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;// retailerId or merchantId

    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;

    @Column(length = 1024)
    private String primaryImageUrl;

    private String location;

    private LocalDateTime createdAt;
}

package com.trademind.billing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill_hashes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillHash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long billId;

    @Column(nullable = false, unique = true)
    private String hashValue;

    private String algorithm;
    private LocalDateTime generatedAt;
}


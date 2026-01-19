package com.trademind.billing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "qr_metadata")
@Getter
@Setter
public class QRCodeMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long billId;

    @Column(columnDefinition = "TEXT")
    private String qrData;
}

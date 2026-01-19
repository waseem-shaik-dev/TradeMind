package com.trademind.merchantorder.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "merchant_order_items")
@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
public class MerchantOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long productId;

    private Integer quantity;
    private BigDecimal price;
}

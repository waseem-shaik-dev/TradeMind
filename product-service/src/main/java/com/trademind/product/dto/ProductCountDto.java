package com.trademind.product.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCountDto {

    private long totalProducts;
    private long activeProducts;
    private long inactiveProducts;
}
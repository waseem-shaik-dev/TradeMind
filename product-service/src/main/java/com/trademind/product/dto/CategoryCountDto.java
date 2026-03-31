package com.trademind.product.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryCountDto {

    private Long categoryId;
    private long productCount;
}
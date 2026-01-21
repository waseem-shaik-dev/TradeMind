package com.trademind.product.dto;

import java.math.BigDecimal;

public record ProductSearchRequest(
        String keyword,

        Long categoryId,
        Long brandId,

        BigDecimal minPrice,
        BigDecimal maxPrice,

        Boolean active
) {}

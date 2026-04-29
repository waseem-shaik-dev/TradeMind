package com.trademind.product.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductSearchRequest(
        String keyword,

        Long categoryId,
        List<Long> categoryIds,
        Long brandId,

        BigDecimal minPrice,
        BigDecimal maxPrice,

        Boolean active
) {}

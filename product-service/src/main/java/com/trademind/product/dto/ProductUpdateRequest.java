package com.trademind.product.dto;

import java.util.List;

public record ProductUpdateRequest(
        String name,
        String description,

        Long categoryId,
        Long brandId,
        Long unitOfMeasureId,

        boolean returnable,
        boolean taxable,
        boolean active,

        List<ProductAttributeRequest> attributes
) {}

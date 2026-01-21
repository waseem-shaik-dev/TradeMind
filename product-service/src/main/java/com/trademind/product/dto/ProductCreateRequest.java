package com.trademind.product.dto;

import java.util.List;

public record ProductCreateRequest(
        String name,
        String sku,
        String description,

        Long categoryId,
        Long brandId,
        Long unitOfMeasureId,

        boolean returnable,
        boolean taxable,

        List<ProductAttributeRequest> attributes
) {}

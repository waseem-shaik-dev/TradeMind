package com.trademind.product.dto;

import com.trademind.product.enums.OwnerType;

import java.util.List;

public record ProductCreateRequest(
        String name,
        String sku,
        String description,

        Long categoryId,
        Long brandId,
        Long unitOfMeasureId,

        Long ownerId,
        OwnerType ownerType,

        boolean returnable,
        boolean taxable,

        List<ProductAttributeRequest> attributes
) {}

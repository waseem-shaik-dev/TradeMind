package com.trademind.product.dto;

import com.trademind.product.enums.OwnerType;

import java.util.List;

public record ProductUpdateRequest(
        String name,
        String description,

        Long categoryId,
        Long brandId,
        Long unitOfMeasureId,

        Long ownerId,
        OwnerType ownerType,

        boolean returnable,
        boolean taxable,
        boolean active,

        List<ProductAttributeRequest> attributes
) {}

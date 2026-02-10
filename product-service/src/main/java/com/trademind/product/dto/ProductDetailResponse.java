package com.trademind.product.dto;

import com.trademind.product.enums.OwnerType;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailResponse(
        Long id,
        String name,
        String sku,
        String description,

        Long categoryId,
        Long brandId,
        Long unitOfMeasureId,

        boolean returnable,
        boolean taxable,
        boolean active,

        BigDecimal currentPrice,

        List<ProductImageResponse> images,
        List<ProductAttributeResponse> attributes,
        Long ownerId,
        OwnerType ownerType
) {}

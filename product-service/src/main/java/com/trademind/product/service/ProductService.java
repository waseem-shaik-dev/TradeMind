package com.trademind.product.service;

import com.trademind.product.dto.*;
import com.trademind.product.enums.OwnerType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    ProductDetailResponse createProduct(ProductCreateRequest request);

    ProductDetailResponse updateProduct(Long productId, ProductUpdateRequest request);

    void deactivateProduct(Long productId);

    List<ProductSummaryResponse> getProductSummariesByOwner(
            Long ownerId,
            OwnerType ownerType
    );

    List<ProductDetailResponse> getProductDetailsByOwner(
            Long ownerId,
            OwnerType ownerType
    );

    ProductPriceResponse setProductPrice(
            Long productId,
            BigDecimal price,
            LocalDateTime effectiveFrom
    );
    List<ProductSummaryResponse> getProductSummariesByOwnerType(OwnerType ownerType);

    ProductDetailResponse getProductDetailById(Long productId);

}

package com.trademind.product.service;

import com.trademind.product.dto.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductService {

    ProductDetailResponse createProduct(ProductCreateRequest request);

    ProductDetailResponse updateProduct(Long productId, ProductUpdateRequest request);

    void deactivateProduct(Long productId);

    ProductPriceResponse setProductPrice(
            Long productId,
            BigDecimal price,
            LocalDateTime effectiveFrom
    );
}

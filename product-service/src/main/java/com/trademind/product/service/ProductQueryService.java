package com.trademind.product.service;

import com.trademind.product.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryService {

    ProductDetailResponse getProductById(Long productId);

    Page<ProductSummaryResponse> searchProducts(
            ProductSearchRequest request,
            Pageable pageable
    );
}

package com.trademind.product.mapper;

import com.trademind.product.dto.ProductPriceResponse;
import com.trademind.product.entity.ProductPriceHistory;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceMapper {

    public ProductPriceResponse toResponse(ProductPriceHistory history) {
        if (history == null) return null;

        return new ProductPriceResponse(
                history.getPrice(),
                history.getEffectiveFrom()
        );
    }
}

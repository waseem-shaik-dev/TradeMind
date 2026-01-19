package com.trademind.product.service;

import com.trademind.product.entity.Product;
import com.trademind.product.entity.ProductPriceHistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product getProduct(Long id);

    List<Product> getActiveProducts();

    ProductPriceHistory setProductPrice(
            Long productId,
            BigDecimal price,
            LocalDateTime effectiveFrom
    );

    BigDecimal getCurrentPrice(Long productId);
}

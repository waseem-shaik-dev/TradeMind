package com.trademind.product.serviceImpl;

import com.trademind.product.entity.Product;
import com.trademind.product.entity.ProductPriceHistory;
import com.trademind.product.repository.ProductPriceHistoryRepository;
import com.trademind.product.repository.ProductRepository;
import com.trademind.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final ProductPriceHistoryRepository priceRepo;

    @Override
    public Product createProduct(Product product) {
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        return productRepo.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> getActiveProducts() {
        return productRepo.findByActiveTrue();
    }

    @Override
    public ProductPriceHistory setProductPrice(
            Long productId,
            BigDecimal price,
            LocalDateTime effectiveFrom) {

        priceRepo.findCurrentPrice(productId)
                .ifPresent(existing -> {
                    existing.setEffectiveTo(effectiveFrom.minusSeconds(1));
                    priceRepo.save(existing);
                });

        ProductPriceHistory history = new ProductPriceHistory();
        history.setProductId(productId);
        history.setPrice(price);
        history.setEffectiveFrom(effectiveFrom);

        return priceRepo.save(history);
    }

    @Override
    public BigDecimal getCurrentPrice(Long productId) {
        return priceRepo.findCurrentPrice(productId)
                .orElseThrow(() -> new RuntimeException("Price not found"))
                .getPrice();
    }
}

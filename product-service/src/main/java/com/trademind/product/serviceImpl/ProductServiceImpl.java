package com.trademind.product.serviceImpl;

import com.trademind.product.dto.*;
import com.trademind.product.entity.Product;
import com.trademind.product.entity.ProductAttribute;
import com.trademind.product.entity.ProductPriceHistory;
import com.trademind.product.mapper.ProductAttributeMapper;
import com.trademind.product.mapper.ProductMapper;
import com.trademind.product.mapper.ProductPriceMapper;
import com.trademind.product.repository.ProductAttributeRepository;
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

    private final ProductRepository productRepository;
    private final ProductAttributeRepository attributeRepository;
    private final ProductPriceHistoryRepository priceHistoryRepository;

    private final ProductMapper productMapper;
    private final ProductAttributeMapper attributeMapper;
    private final ProductPriceMapper priceMapper;

    @Override
    public ProductDetailResponse createProduct(ProductCreateRequest request) {

        if (productRepository.existsBySku(request.sku())) {
            throw new RuntimeException("SKU already exists");
        }

        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);

        List<ProductAttribute> savedAttributes = List.of();

        if (request.attributes() != null && !request.attributes().isEmpty()) {
            savedAttributes = request.attributes().stream()
                    .map(attributeMapper::toEntity)
                    .peek(attr -> attr.setProductId(savedProduct.getId()))
                    .toList();

            attributeRepository.saveAll(savedAttributes);
        }

        BigDecimal currentPrice =
                priceHistoryRepository.findCurrentPrice(savedProduct.getId())
                        .map(ProductPriceHistory::getPrice)
                        .orElse(null);

        return productMapper.toDetailResponse(
                savedProduct,
                currentPrice,
                savedAttributes
        );
    }


    @Override
    public ProductDetailResponse updateProduct(Long productId, ProductUpdateRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productMapper.updateEntity(request, product);
        Product updated = productRepository.save(product);

        List<ProductAttribute> updatedAttributes = List.of();

        if (request.attributes() != null) {
            attributeRepository.deleteAll(
                    attributeRepository.findByProductId(productId)
            );

            updatedAttributes = request.attributes().stream()
                    .map(attributeMapper::toEntity)
                    .peek(attr -> attr.setProductId(productId))
                    .toList();

            attributeRepository.saveAll(updatedAttributes);
        } else {
            updatedAttributes = attributeRepository.findByProductId(productId);
        }

        BigDecimal currentPrice =
                priceHistoryRepository.findCurrentPrice(productId)
                        .map(ProductPriceHistory::getPrice)
                        .orElse(null);

        return productMapper.toDetailResponse(
                updated,
                currentPrice,
                updatedAttributes
        );
    }


    @Override
    public void deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public ProductPriceResponse setProductPrice(
            Long productId,
            BigDecimal price,
            LocalDateTime effectiveFrom) {

        priceHistoryRepository.findCurrentPrice(productId)
                .ifPresent(existing -> {
                    existing.setEffectiveTo(effectiveFrom.minusSeconds(1));
                    priceHistoryRepository.save(existing);
                });

        ProductPriceHistory history = new ProductPriceHistory();
        history.setProductId(productId);
        history.setPrice(price);
        history.setEffectiveFrom(effectiveFrom);

        return priceMapper.toResponse(
                priceHistoryRepository.save(history)
        );
    }
}

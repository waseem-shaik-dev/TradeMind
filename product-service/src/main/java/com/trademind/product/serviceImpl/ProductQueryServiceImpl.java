package com.trademind.product.serviceImpl;

import com.trademind.product.dto.*;
import com.trademind.product.entity.Product;
import com.trademind.product.mapper.ProductMapper;
import com.trademind.product.repository.ProductAttributeRepository;
import com.trademind.product.repository.ProductPriceHistoryRepository;
import com.trademind.product.repository.ProductRepository;
import com.trademind.product.repository.spec.ProductSpecification;
import com.trademind.product.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final ProductPriceHistoryRepository priceHistoryRepository;
    private final ProductMapper productMapper;
    private final ProductAttributeRepository attributeRepository;

    @Override
    public ProductDetailResponse getProductById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        BigDecimal currentPrice =
                priceHistoryRepository.findCurrentPrice(productId)
                        .map(p -> p.getPrice())
                        .orElse(null);

        var attributes = attributeRepository.findByProductId(productId);

        return productMapper.toDetailResponse(
                product,
                currentPrice,
                attributes
        );
    }


    @Override
    public Page<ProductSummaryResponse> searchProducts(
            ProductSearchRequest request,
            Pageable pageable) {

        Page<Product> page = productRepository.findAll(
                ProductSpecification.filter(
                        request.keyword(),
                        request.categoryId(),
                        request.brandId(),
                        request.active()
                ),
                pageable
        );

        return page.map(product -> {
            var summary = new ProductSummaryResponse(
                    product.getId(),
                    product.getName(),
                    product.getSku(),
                    priceHistoryRepository.findCurrentPrice(product.getId())
                            .map(p -> p.getPrice())
                            .orElse(null),
                    product.getImages() != null
                            ? product.getImages().stream()
                            .filter(i -> i.isPrimaryImage())
                            .findFirst()
                            .map(i -> i.getImageUrl())
                            .orElse(null)
                            : null
            );
            return summary;
        });
    }
}

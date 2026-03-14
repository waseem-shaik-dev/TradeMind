package com.trademind.product.serviceImpl;

import com.trademind.product.dto.*;
import com.trademind.product.dto.internal.CatalogueProductForCartResponse;
import com.trademind.product.entity.Product;
import com.trademind.product.entity.ProductAttribute;
import com.trademind.product.entity.ProductImage;
import com.trademind.product.entity.ProductPriceHistory;
import com.trademind.product.enums.OwnerType;
import com.trademind.product.mapper.ProductAttributeMapper;
import com.trademind.product.mapper.ProductMapper;
import com.trademind.product.mapper.ProductPriceMapper;
import com.trademind.product.repository.ProductAttributeRepository;
import com.trademind.product.repository.ProductImageRepository;
import com.trademind.product.repository.ProductPriceHistoryRepository;
import com.trademind.product.repository.ProductRepository;
import com.trademind.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductAttributeRepository attributeRepository;
    private final ProductPriceHistoryRepository priceHistoryRepository;
    private final ProductImageRepository productImageRepository;

    private final ProductMapper productMapper;
    private final ProductAttributeMapper attributeMapper;
    private final ProductPriceMapper priceMapper;
    private final Executor productExecutor;


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

//    @Override
//    public List<ProductSummaryResponse> getProductSummariesByOwnerType(
//            OwnerType ownerType) {
//
//        return productRepository.findByOwnerType(ownerType)
//                .stream()
//                .map(product -> {
//
//                    BigDecimal currentPrice =
//                            priceHistoryRepository
//                                    .findCurrentPrice(product.getId())
//                                    .map(ProductPriceHistory::getPrice)
//                                    .orElse(null);
//
//                    String primaryImageUrl =
//                            product.getImages().stream()
//                                    .filter(ProductImage::isPrimaryImage)
//                                    .map(ProductImage::getImageUrl)
//                                    .findFirst()
//                                    .orElse(null);
//
//                    return new ProductSummaryResponse(
//                            product.getId(),
//                            product.getName(),
//                            product.getSku(),
//                            currentPrice,
//                            primaryImageUrl,
//                            product.getOwnerId(),
//                            product.getOwnerType()
//                    );
//                })
//                .toList();
//    }

    @Override
    public ProductDetailResponse getProductDetailById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        BigDecimal currentPrice =
                priceHistoryRepository
                        .findCurrentPrice(productId)
                        .map(ProductPriceHistory::getPrice)
                        .orElse(null);

        List<ProductAttribute> attributes =
                attributeRepository.findByProductId(productId);

        return productMapper.toDetailResponse(
                product,
                currentPrice,
                attributes
        );
    }


//    @Override
//    public List<ProductSummaryResponse> getProductSummariesByOwner(
//            Long ownerId,
//            OwnerType ownerType) {
//
//        return productRepository
//                .findByOwnerIdAndOwnerType(ownerId, ownerType)
//                .stream()
//                .map(product -> {
//
//                    // 🔹 Current price from price history
//                    BigDecimal currentPrice =
//                            priceHistoryRepository
//                                    .findCurrentPrice(product.getId())
//                                    .map(ProductPriceHistory::getPrice)
//                                    .orElse(null);
//
//                    // 🔹 Primary image from product images
//                    String primaryImageUrl =
//                            product.getImages()
//                                    .stream()
//                                    .filter(ProductImage::isPrimaryImage)
//                                    .map(ProductImage::getImageUrl)
//                                    .findFirst()
//                                    .orElse(null);
//
//                    return new ProductSummaryResponse(
//                            product.getId(),
//                            product.getName(),
//                            product.getSku(),
//                            currentPrice,
//                            primaryImageUrl,
//                            product.getOwnerId(),
//                            product.getOwnerType()
//                    );
//                })
//                .toList();
//    }



//    @Override
//    public List<ProductDetailResponse> getProductDetailsByOwner(
//            Long ownerId,
//            OwnerType ownerType) {
//
//        return productRepository
//                .findByOwnerIdAndOwnerType(ownerId, ownerType)
//                .stream()
//                .map(product -> {
//
//                    BigDecimal currentPrice =
//                            priceHistoryRepository
//                                    .findCurrentPrice(product.getId())
//                                    .map(ProductPriceHistory::getPrice)
//                                    .orElse(null);
//
//                    List<ProductAttribute> attributes =
//                            attributeRepository.findByProductId(product.getId());
//
//                    return productMapper.toDetailResponse(
//                            product,
//                            currentPrice,
//                            attributes
//                    );
//                })
//                .toList();
//    }


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

    @Override
    @Transactional(readOnly = true)
    public List<CatalogueProductForCartResponse> getProductsForCart(
            List<Long> productIds) {

        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        // 1️⃣ Fetch products in batch
        List<Product> products =
                productRepository.findByIdIn(productIds);

        // 2️⃣ Fetch images in batch (FIXED & SORTED)
        Map<Long, List<String>> imageMap =
                productImageRepository.findByProductIdIn(productIds)
                        .stream()
                        .sorted(
                                java.util.Comparator
                                        .comparing(ProductImage::isPrimaryImage).reversed()
                                        .thenComparing(
                                                ProductImage::getDisplayOrder,
                                                java.util.Comparator.nullsLast(Integer::compareTo)
                                        )
                        )
                        .collect(Collectors.groupingBy(
                                img -> img.getProduct().getId(),   // ✅ CORRECT
                                Collectors.mapping(
                                        ProductImage::getImageUrl,
                                        Collectors.toList()
                                )
                        ));

        // 3️⃣ Fetch current prices in batch (uses YOUR time-aware logic)
        Map<Long, BigDecimal> priceMap =
                priceHistoryRepository.findCurrentPrices(productIds)
                        .stream()
                        .collect(Collectors.toMap(
                                ProductPriceHistory::getProductId,
                                ProductPriceHistory::getPrice
                        ));

        // 4️⃣ Map to CatalogueProductForCartResponse
        return products.stream()
                .map(product -> new CatalogueProductForCartResponse(
                        product.getId(),
                        product.getName(),
                        product.getSku(),
                        priceMap.get(product.getId()),
                        imageMap.getOrDefault(product.getId(), List.of())
                ))
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<SellerProductViewResponse> getProductsForSeller() {

        // 1️⃣ Fetch all active products
        List<SellerProductProjection> products =
                productRepository.findSellerProductProjection();

        if(products.isEmpty()){
            return List.of();
        }

        List<Long> productIds =
                products.stream()
                        .map(SellerProductProjection::id)
                        .toList();




        // 2️⃣ Fetch attributes in batch
        CompletableFuture<Map<Long, Map<String,String>>> attributeMapFuture =
               CompletableFuture.supplyAsync(
                       ()->
                           attributeRepository.findByProductIdIn(productIds)
                                   .stream()
                                   .collect(Collectors.groupingBy(
                                           ProductAttribute::getProductId,
                                           Collectors.toMap(
                                                   ProductAttribute::getAttributeName,
                                                   ProductAttribute::getAttributeValue
                                           )
                                   )),
                       productExecutor
               );


        // 3️⃣ Fetch images in batch
        CompletableFuture<Map<Long,String>> imageMapFuture =
                CompletableFuture.supplyAsync(
                        ()->
                                productImageRepository.findByProductIdIn(productIds)
                                .stream()
                                .filter(ProductImage::isPrimaryImage)
                                .collect(Collectors.toMap(
                                        img -> img.getProduct().getId(),
                                        ProductImage::getImageUrl,
                                        (a,b) -> a
                                )),
                        productExecutor
                );


        // 4️⃣ Fetch prices in batch
        CompletableFuture<Map<Long, BigDecimal>> priceMapFuture =
                CompletableFuture.supplyAsync(
                        ()->
                                priceHistoryRepository.findCurrentPrices(productIds)
                                        .stream()
                                        .collect(Collectors.toMap(
                                                ProductPriceHistory::getProductId,
                                                ProductPriceHistory::getPrice
                                        )),
                        productExecutor
                );


        CompletableFuture.allOf(priceMapFuture,imageMapFuture,attributeMapFuture).join();

        Map<Long, Map<String,String>> attributeMap = attributeMapFuture.join();
        Map<Long,String> imageMap = imageMapFuture.join();
        Map<Long, BigDecimal> priceMap = priceMapFuture.join();


        // 5️⃣ Map to response
        return products.stream()
                .map(product -> new SellerProductViewResponse(

                        product.id(),
                        product.name(),
                        product.sku(),
                        product.description(),

                        priceMap.get(product.id()),

                        imageMap.get(product.id()),

                        attributeMap.getOrDefault(
                                product.id(),
                                Map.of()
                        )

                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductSummaryResponse> getProductSummaries(
            List<Long> productIds) {

        List<Product> products =
                productRepository.findByIdIn(productIds);

        Map<Long, BigDecimal> priceMap =
                priceHistoryRepository.findCurrentPrices(productIds)
                        .stream()
                        .collect(Collectors.toMap(
                                ProductPriceHistory::getProductId,
                                ProductPriceHistory::getPrice
                        ));

        Map<Long, String> imageMap =
                productImageRepository.findByProductIdIn(productIds)
                        .stream()
                        .filter(ProductImage::isPrimaryImage)
                        .collect(Collectors.toMap(
                                img -> img.getProduct().getId(),
                                ProductImage::getImageUrl,
                                (a,b)->a
                        ));

        return products.stream()
                .map(p -> new ProductSummaryResponse(
                        p.getId(),
                        p.getName(),
                        p.getSku(),
                        priceMap.get(p.getId()),
                        imageMap.get(p.getId())
                ))
                .toList();
    }
}

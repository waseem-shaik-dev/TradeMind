package com.trademind.catalogue.serviceImpl;

import com.trademind.catalogue.dto.*;
import com.trademind.catalogue.service.CatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogueServiceImpl implements CatalogueService {

    private final RestClient restClient;

    private static final String PRODUCT_URL = "http://localhost:8083/api/products";
    private static final String INVENTORY_URL = "http://localhost:8084/api/inventories";

    @Override
    public List<CatalogueProductSummaryResponse> browseMerchantProducts() {
        return browseByOwnerType("MERCHANT");
    }

    @Override
    public List<CatalogueProductSummaryResponse> browseRetailerProducts() {
        return browseByOwnerType("RETAILER");
    }

    private List<CatalogueProductSummaryResponse> browseByOwnerType(
            String ownerType) {

        // 1️⃣ Fetch inventory first
        List<CatalogueInventoryResponse> inventory =
                restClient.get()
                        .uri(INVENTORY_URL + "/catalogue?ownerType=" + ownerType)
                        .retrieve()
                        .body(new ParameterizedTypeReference<
                                List<CatalogueInventoryResponse>>() {});

        if (inventory == null || inventory.isEmpty()) {
            return List.of();
        }

        // 2️⃣ Extract productIds
        List<Long> productIds =
                inventory.stream()
                        .map(CatalogueInventoryResponse::productId)
                        .toList();

        // 3️⃣ Fetch products in batch
        List<ProductSummaryResponse> products =
                restClient.post()
                        .uri(PRODUCT_URL + "/internal/products/summary")
                        .body(productIds)
                        .retrieve()
                        .body(new ParameterizedTypeReference<
                                List<ProductSummaryResponse>>() {});

        Map<Long, CatalogueInventoryResponse> inventoryMap =
                inventory.stream()
                        .collect(Collectors.toMap(
                                CatalogueInventoryResponse::productId,
                                i -> i
                        ));

        return products.stream()
                .map(p -> {

                    CatalogueInventoryResponse stock =
                            inventoryMap.get(p.id());

                    return new CatalogueProductSummaryResponse(
                            p.id(),
                            p.name(),
                            p.sku(),
                            p.currentPrice(),
                            p.primaryImageUrl(),
                            stock.quantityAvailable(),
                            stock.outOfStock()
                    );

                })
                .toList();
    }

    @Override
    public CatalogueProductDetailResponse getCatalogueProductDetail(
            Long productId) {

        ProductDetailResponse product =
                restClient.get()
                        .uri(PRODUCT_URL + "/catalogue/{id}", productId)
                        .retrieve()
                        .body(ProductDetailResponse.class);

        CatalogueInventoryResponse stock =
                restClient.get()
                        .uri(INVENTORY_URL + "/catalogue/{id}", productId)
                        .retrieve()
                        .body(CatalogueInventoryResponse.class);


        return new CatalogueProductDetailResponse(
                product.id(),
                product.name(),
                product.sku(),
                product.description(),
                product.currentPrice(),
                product.returnable(),
                product.taxable(),
                product.images()
                        .stream()
                        .map(ProductImageResponse::imageUrl)
                        .toList(),
                product.attributes(),
                stock.quantityAvailable(),
                stock.outOfStock()
        );
    }

    @Override
    public CatalogueProductForCartResponse getProductForCart(Long productId) {

        ProductDetailResponse product =
                restClient.get()
                        .uri(PRODUCT_URL + "/catalogue/{id}", productId)
                        .retrieve()
                        .body(ProductDetailResponse.class);

        return new CatalogueProductForCartResponse(
                product.id(),
                product.name(),
                product.sku(),
                product.currentPrice(),
                product.images()
                        .stream()
                        .map(ProductImageResponse::imageUrl)
                        .toList()
        );
    }

    @Override
    public List<CatalogueProductForCartResponse> getProductsForCart(
            List<Long> productIds) {

        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        return restClient.post()
                .uri("http://localhost:8083/internal/products/batch")
                .body(productIds)
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<
                        List<CatalogueProductForCartResponse>>() {});
    }


    @Override
    public List<SellerCatalogueProductResponse> browseProductsForSeller(
            Long sourceId) {

        CompletableFuture<List<SellerProductViewResponse>> productsFuture =
                CompletableFuture.supplyAsync(() ->
                        restClient.get()
                                .uri(PRODUCT_URL + "/seller/products")
                                .retrieve()
                                .body(new ParameterizedTypeReference<
                                        List<SellerProductViewResponse>>() {})
                );

        CompletableFuture<List<SellerInventoryViewResponse>> inventoryFuture =
                CompletableFuture.supplyAsync(() ->
                        restClient.get()
                                .uri(INVENTORY_URL + "/seller/{id}", sourceId)
                                .retrieve()
                                .body(new ParameterizedTypeReference<
                                                                        List<SellerInventoryViewResponse>>() {})
                );

        CompletableFuture.allOf(productsFuture, inventoryFuture).join();

        List<SellerProductViewResponse> products = productsFuture.join();
        List<SellerInventoryViewResponse> inventory = inventoryFuture.join();

        Map<Long, SellerInventoryViewResponse> inventoryMap =
                inventory.stream()
                        .collect(Collectors.toMap(
                                SellerInventoryViewResponse::productId,
                                i -> i,
                                (a,b) -> a
                        ));

        return products.stream()
                .map(product -> {

                    SellerInventoryViewResponse inv =
                            inventoryMap.get(product.id());

                    return SellerCatalogueProductResponse.builder()
                            .productId(product.id())
                            .name(product.name())
                            .sku(product.sku())
                            .description(product.description())
                            .currentPrice(product.currentPrice())
                            .primaryImageUrl(product.primaryImageUrl())
                            .attributes(product.attributes())
                            .sellerHasInventory(inv != null)
                            .inventoryId(inv != null ? inv.inventoryId() : null)
                            .availableQuantity(inv != null ? inv.availableQuantity() : 0)
                            .sellerPrice(inv != null ? inv.price() : null)
                            .build();
                })
                .toList();
    }


}

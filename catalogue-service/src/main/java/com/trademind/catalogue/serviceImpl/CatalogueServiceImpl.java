package com.trademind.catalogue.serviceImpl;

import com.trademind.catalogue.dto.*;
import com.trademind.catalogue.service.CatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
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

        List<ProductSummaryResponse> products =
                restClient.get()
                        .uri(PRODUCT_URL + "/catalogue/summary?ownerType=" + ownerType)
                        .retrieve()
                        .body(new org.springframework.core.ParameterizedTypeReference<
                                List<ProductSummaryResponse>>() {});

        List<CatalogueInventoryResponse> inventory =
                restClient.get()
                        .uri(INVENTORY_URL + "/catalogue?ownerType=" + ownerType)
                        .retrieve()
                        .body(new org.springframework.core.ParameterizedTypeReference<
                                List<CatalogueInventoryResponse>>() {});

        Map<Long, CatalogueInventoryResponse> inventoryMap =
                inventory.stream()
                        .collect(Collectors.toMap(
                                CatalogueInventoryResponse::productId,
                                i -> i,
                                (a, b) -> a
                        ));

        return products.stream()
                .map(p -> {
                    CatalogueInventoryResponse stock = inventoryMap.get(p.id());
                    return new CatalogueProductSummaryResponse(
                            p.id(),
                            p.name(),
                            p.sku(),
                            p.currentPrice(),
                            p.primaryImageUrl(),
                            stock != null ? stock.quantityAvailable() : 0,
                            stock != null && stock.outOfStock()
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
                stock.reservedQuantity(),
                stock.outOfStock()
        );
    }
}

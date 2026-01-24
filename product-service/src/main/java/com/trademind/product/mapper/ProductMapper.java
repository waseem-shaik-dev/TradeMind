package com.trademind.product.mapper;

import com.trademind.product.dto.ProductCreateRequest;
import com.trademind.product.dto.ProductDetailResponse;
import com.trademind.product.dto.ProductUpdateRequest;
import com.trademind.product.entity.Product;
import com.trademind.product.entity.ProductAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ProductImageMapper imageMapper;
    private final ProductAttributeMapper attributeMapper;

    /* -------- CREATE -------- */

    public Product toEntity(ProductCreateRequest request) {
        if (request == null) return null;

        Product product = Product.builder()
                .name(request.name())
                .sku(request.sku())
                .description(request.description())
                .categoryId(request.categoryId())
                .brandId(request.brandId())
                .unitOfMeasureId(request.unitOfMeasureId())
                .ownerId(request.ownerId())
                .ownerType(request.ownerType())
                .returnable(request.returnable())
                .taxable(request.taxable())
                .build();

        return product;
    }

    /* -------- UPDATE -------- */

    public void updateEntity(ProductUpdateRequest request, Product product) {
        if (request == null || product == null) return;

        if (request.name() != null) product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.categoryId() != null) product.setCategoryId(request.categoryId());
        if (request.brandId() != null) product.setBrandId(request.brandId());
        if (request.unitOfMeasureId() != null) product.setUnitOfMeasureId(request.unitOfMeasureId());
        if(request.ownerId()!=null)product.setOwnerId(request.ownerId());
        if(request.ownerType()!=null)product.setOwnerType(request.ownerType());

        product.setReturnable(request.returnable());
        product.setTaxable(request.taxable());
        product.setActive(request.active());
    }

    /* -------- RESPONSES -------- */

    public ProductDetailResponse toDetailResponse(
            Product product,
            BigDecimal currentPrice,
            List<ProductAttribute> attributes) {

        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getDescription(),
                product.getCategoryId(),
                product.getBrandId(),
                product.getUnitOfMeasureId(),
                product.isReturnable(),
                product.isTaxable(),
                product.isActive(),
                currentPrice,
                imageMapper.toResponses(product.getImages()),
                attributeMapper.toResponses(attributes)
        );
    }
}

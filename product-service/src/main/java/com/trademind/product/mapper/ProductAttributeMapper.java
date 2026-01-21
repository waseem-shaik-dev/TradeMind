package com.trademind.product.mapper;

import com.trademind.product.dto.ProductAttributeRequest;
import com.trademind.product.dto.ProductAttributeResponse;
import com.trademind.product.entity.ProductAttribute;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductAttributeMapper {

    public ProductAttribute toEntity(ProductAttributeRequest request) {
        if (request == null) return null;

        ProductAttribute attr = new ProductAttribute();
        attr.setAttributeName(request.attributeName());
        attr.setAttributeValue(request.attributeValue());
        return attr;
    }

    public ProductAttributeResponse toResponse(ProductAttribute attribute) {
        if (attribute == null) return null;

        return new ProductAttributeResponse(
                attribute.getAttributeName(),
                attribute.getAttributeValue()
        );
    }

    public List<ProductAttributeResponse> toResponses(List<ProductAttribute> attributes) {
        if (attributes == null) return List.of();

        return attributes.stream()
                .map(this::toResponse)
                .toList();
    }
}

package com.trademind.product.mapper;

import com.trademind.product.dto.ProductImageResponse;
import com.trademind.product.entity.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProductImageMapper {

    public ProductImageResponse toResponse(ProductImage image) {
        if (image == null) return null;

        return new ProductImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.isPrimaryImage(),
                image.getDisplayOrder()==null?0: image.getDisplayOrder()
        );
    }

    public List<ProductImageResponse> toResponses(List<ProductImage> images) {
        if (images == null) return List.of();

        return images.stream()
                .sorted((a, b) -> Integer.compare(a.getDisplayOrder(), b.getDisplayOrder()))
                .map(this::toResponse)
                .toList();
    }
}

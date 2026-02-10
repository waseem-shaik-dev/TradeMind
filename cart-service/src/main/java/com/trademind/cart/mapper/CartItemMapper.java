package com.trademind.cart.mapper;

import com.trademind.cart.client.CatalogueClient;
import com.trademind.cart.dto.CartItemResponseDto;
import com.trademind.cart.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartItemMapper {

    public CartItemResponseDto toDto(
            CartItem item,
            CatalogueClient.CatalogueProductDto product,
            Integer availableQty
    ) {

        boolean outOfStock = availableQty <= 0;
        boolean available = availableQty >= item.getQuantity();

        boolean priceChanged =
                product.getPrice().compareTo(item.getUnitPrice()) != 0;

        BigDecimal total =
                item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

        String primaryImage =
                (product.getImages() != null && !product.getImages().isEmpty())
                        ? product.getImages().get(0)
                        : null;

        return new CartItemResponseDto(
                item.getId(),
                item.getProductId(),
                product.getName(),
                product.getSku(),
                primaryImage,
                product.getImages(),
                item.getUnitPrice(),
                item.getQuantity(),
                total,
                available,
                priceChanged,
                outOfStock
        );
    }

    public CartItemResponseDto toUnavailableDto(CartItem item) {
        return new CartItemResponseDto(
                item.getId(),
                item.getProductId(),
                "Product unavailable",
                null,
                null,
                List.of(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())),
                false,
                false,
                true
        );
    }

}

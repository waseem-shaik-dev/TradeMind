package com.trademind.cart.mapper;

import com.trademind.cart.client.UserClient;
import com.trademind.cart.dto.*;
import com.trademind.cart.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final UserClient userClient;

    public CartSourceDto mapSource(Cart cart) {

        var source =
                userClient.getCartSourceInfo(
                        cart.getSourceId(),
                        cart.getSourceType().name()
                );

        return new CartSourceDto(
                source.sourceId(),
                source.sourceType(),
                source.name(),
                source.logo()
        );
    }

    public CartResponseDto toCartResponse(
            Cart cart,
            CartSourceDto source,
            List<CartItemResponseDto> items,
            CartPriceSummaryDto priceSummary
    ) {

        return new CartResponseDto(
                cart.getId(),
                cart.getUserId(),
                source,
                cart.getStatus().name(),
                cart.isActive(),
                items,
                priceSummary,
                null,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }

    public CartSummaryResponseDto toSummary(
            Cart cart,
            CartSourceDto source,
            CartPriceSummaryDto priceSummary
    ) {

        return new CartSummaryResponseDto(
                cart.getId(),
                source,
                priceSummary.totalItems(),
                priceSummary.totalQuantity(),
                priceSummary.subTotal(),
                cart.isActive()
        );
    }
}

package com.trademind.cart.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trademind.cart.client.UserClient;
import com.trademind.cart.dto.*;
import com.trademind.cart.entity.Cart;
import com.trademind.events.common.SellerSnapshotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final UserClient userClient;
    private final ObjectMapper objectMapper;

    // ✅ READ FROM SNAPSHOT (NO FEIGN)
    public SellerSnapshotDto mapSeller(Cart cart) {

        try {
            return objectMapper.readValue(
                    cart.getSourceSnapshot(),
                    SellerSnapshotDto.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse seller snapshot", e);
        }
    }

    public CartResponseDto toCartResponse(
            Cart cart,
            SellerSnapshotDto seller,
            List<CartItemResponseDto> items,
            CartPriceSummaryDto priceSummary,
            CartValidationDto validationDto
    ) {

        return new CartResponseDto(
                cart.getId(),
                cart.getUserId(),
                seller,
                cart.getStatus().name(),
                cart.isActive(),
                items,
                priceSummary,
                validationDto,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }

    public CartSummaryResponseDto toSummary(
            Cart cart,
            SellerSnapshotDto seller,
            CartPriceSummaryDto priceSummary
    ) {

        return new CartSummaryResponseDto(
                cart.getId(),
                seller,
                priceSummary.totalItems(),
                priceSummary.totalQuantity(),
                priceSummary.subTotal(),
                cart.isActive()
        );
    }
}

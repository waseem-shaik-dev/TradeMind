package com.trademind.cart.mapper;

import com.trademind.cart.dto.CartItemResponseDto;
import com.trademind.cart.dto.CartPriceSummaryDto;
import com.trademind.cart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartPriceMapper {

    public CartPriceSummaryDto toPriceSummary(
            List<CartItemResponseDto> items) {

        BigDecimal subTotal = BigDecimal.ZERO;
        int totalQty = 0;

        for (CartItemResponseDto item : items) {
            subTotal = subTotal.add(item.totalPrice());
            totalQty += item.quantity();
        }

        return new CartPriceSummaryDto(
                subTotal,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                subTotal,
                items.size(),
                totalQty
        );
    }

    public CartPriceSummaryDto fromCartItems(List<CartItem> items) {

        BigDecimal subTotal = BigDecimal.ZERO;
        int totalQty = 0;

        for (CartItem item : items) {
            subTotal = subTotal.add(
                    item.getUnitPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
            );
            totalQty += item.getQuantity();
        }

        return new CartPriceSummaryDto(
                subTotal,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                subTotal,
                items.size(),
                totalQty
        );
    }

}

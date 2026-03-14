package com.trademind.checkout.mapper;

import com.trademind.checkout.dto.response.CheckoutItemResponseDto;
import com.trademind.checkout.entity.CheckoutItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CheckoutItemMapper {

    public CheckoutItemResponseDto toResponseDto(CheckoutItem item) {
        return new CheckoutItemResponseDto(
                item.getProductId(),
                item.getProductName(),
                item.getSku(),
                item.getImageUrl(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }

    public List<CheckoutItemResponseDto> toResponseDtoList(List<CheckoutItem> items) {
        return items.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}

package com.trademind.checkout.mapper;

import com.trademind.checkout.dto.response.CheckoutPriceSummaryDto;
import com.trademind.checkout.dto.response.CheckoutSummaryResponseDto;
import com.trademind.checkout.entity.CheckoutItem;
import com.trademind.checkout.entity.CheckoutSession;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckoutSessionMapper {

    public CheckoutSummaryResponseDto toSummaryResponse(
            CheckoutSession session
    ) {
        return new CheckoutSummaryResponseDto(
                session.getId(),
                session.getStatus()
        );
    }

    public CheckoutPriceSummaryDto toPriceSummary(
            CheckoutSession session
    ) {
        List<CheckoutItem> items = session.getItems();

        int totalItems = items.size();
        int totalQuantity = items.stream()
                .mapToInt(CheckoutItem::getQuantity)
                .sum();

        return new CheckoutPriceSummaryDto(
                session.getSubtotalAmount(),
                session.getTaxAmount(),
                session.getDiscountAmount(),
                session.getDeliveryFee(),
                session.getGrandTotal(),
                totalItems,
                totalQuantity
        );
    }
}

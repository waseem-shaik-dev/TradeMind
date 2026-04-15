package com.trademind.checkout.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trademind.checkout.dto.response.CheckoutResponseDto;
import com.trademind.checkout.entity.CheckoutSession;
import com.trademind.events.common.SellerSnapshotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckoutAggregateMapper {

    private final CheckoutItemMapper itemMapper;
    private final CheckoutAddressMapper addressMapper;
    private final CheckoutPaymentMapper paymentMapper;
    private final CheckoutSessionMapper sessionMapper;
    private final ObjectMapper objectMapper;

    public CheckoutResponseDto toCheckoutResponse(
            CheckoutSession session
    ) {
        return new CheckoutResponseDto(
                session.getId(),
                session.getCartId(),
                session.getStatus(),
                parseSeller(session.getSellerSnapshot()),
                addressMapper.toResponseDto(session.getAddressSnapshot()),
                paymentMapper.toResponseDto(session.getPaymentSnapshot()),
                itemMapper.toResponseDtoList(session.getItems()),
                sessionMapper.toPriceSummary(session),
                session.getExpiresAt()
        );
    }
    private SellerSnapshotDto parseSeller(String json) {
        try {
            return objectMapper.readValue(json, SellerSnapshotDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse seller snapshot", e);
        }
    }
}

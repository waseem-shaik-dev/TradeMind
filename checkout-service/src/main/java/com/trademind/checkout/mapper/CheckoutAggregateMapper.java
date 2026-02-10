package com.trademind.checkout.mapper;

import com.trademind.checkout.dto.response.CheckoutResponseDto;
import com.trademind.checkout.entity.CheckoutSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckoutAggregateMapper {

    private final CheckoutItemMapper itemMapper;
    private final CheckoutAddressMapper addressMapper;
    private final CheckoutPaymentMapper paymentMapper;
    private final CheckoutSessionMapper sessionMapper;

    public CheckoutResponseDto toCheckoutResponse(
            CheckoutSession session
    ) {
        return new CheckoutResponseDto(
                session.getId(),
                session.getCartId(),
                session.getStatus(),
                addressMapper.toResponseDto(session.getAddressSnapshot()),
                paymentMapper.toResponseDto(session.getPaymentSnapshot()),
                itemMapper.toResponseDtoList(session.getItems()),
                sessionMapper.toPriceSummary(session),
                session.getExpiresAt()
        );
    }
}

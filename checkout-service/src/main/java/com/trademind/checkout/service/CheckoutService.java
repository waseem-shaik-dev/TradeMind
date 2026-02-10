package com.trademind.checkout.service;

import com.trademind.checkout.dto.request.*;
import com.trademind.checkout.dto.response.*;
import com.trademind.checkout.entity.CheckoutSession;
import com.trademind.checkout.enums.CheckoutStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckoutService {

    CheckoutSummaryResponseDto createCheckout(CreateCheckoutRequestDto request,Long userId);

    CheckoutResponseDto getCheckout(Long checkoutId, Long userId);

    CheckoutSummaryResponseDto selectAddress(SelectAddressRequestDto request, Long userId);

    CheckoutSummaryResponseDto selectPaymentMethod(
            SelectPaymentMethodRequestDto request,
            Long userId
    );

    CheckoutSummaryResponseDto confirmCheckout(Long checkoutId, Long userId);

    CheckoutSummaryResponseDto cancelCheckout(
            CancelCheckoutRequestDto request,
            Long userId
    );

    void expireCheckout(Long checkoutId);


}

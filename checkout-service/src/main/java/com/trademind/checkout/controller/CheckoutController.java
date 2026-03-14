package com.trademind.checkout.controller;

import com.trademind.checkout.dto.request.*;
import com.trademind.checkout.dto.response.*;
import com.trademind.checkout.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    // ------------------------------------------------------------------
    // 1️⃣ Create Checkout (from Cart)
    // ------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<CheckoutSummaryResponseDto> createCheckout(
            @RequestBody CreateCheckoutRequestDto request,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("x-user-role") String userRole
    ) {
        CheckoutSummaryResponseDto response =
                checkoutService.createCheckout(request,userId,userRole);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ------------------------------------------------------------------
    // 2️⃣ Get Checkout Page (Amazon-style checkout screen)
    // ------------------------------------------------------------------
    @GetMapping("/{checkoutId}")
    public ResponseEntity<CheckoutResponseDto> getCheckout(
            @PathVariable Long checkoutId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        CheckoutResponseDto response =
                checkoutService.getCheckout(checkoutId, userId);

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------
    // 3️⃣ Select Delivery Address
    // ------------------------------------------------------------------
    @PutMapping("/{checkoutId}/address")
    public ResponseEntity<CheckoutSummaryResponseDto> selectAddress(
            @PathVariable Long checkoutId,
            @RequestBody SelectAddressRequestDto request,
            @RequestHeader("X-User-Id") Long userId
    ) {
        // ensure path + body consistency
        SelectAddressRequestDto finalRequest =
                new SelectAddressRequestDto(checkoutId, request.addressId());

        CheckoutSummaryResponseDto response =
                checkoutService.selectAddress(finalRequest, userId);

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------
    // 4️⃣ Select Payment Method (COD / ONLINE)
    // ------------------------------------------------------------------
    @PutMapping("/{checkoutId}/payment")
    public ResponseEntity<CheckoutSummaryResponseDto> selectPaymentMethod(
            @PathVariable Long checkoutId,
            @RequestBody SelectPaymentMethodRequestDto request,
            @RequestHeader("X-User-Id") Long userId
    ) {
        SelectPaymentMethodRequestDto finalRequest =
                new SelectPaymentMethodRequestDto(
                        checkoutId,
                        request.paymentMethod()
                );

        CheckoutSummaryResponseDto response =
                checkoutService.selectPaymentMethod(finalRequest, userId);

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------
    // 5️⃣ Confirm Checkout (Reserve Inventory + Start Payment)
    // ------------------------------------------------------------------
    @PostMapping("/{checkoutId}/confirm")
    public ResponseEntity<CheckoutSummaryResponseDto> confirmCheckout(
            @PathVariable Long checkoutId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        CheckoutSummaryResponseDto response =
                checkoutService.confirmCheckout(checkoutId, userId);

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------
    // 6️⃣ Cancel Checkout (User / System)
    // ------------------------------------------------------------------
    @PostMapping("/{checkoutId}/cancel")
    public ResponseEntity<CheckoutSummaryResponseDto> cancelCheckout(
            @PathVariable Long checkoutId,
            @RequestBody(required = false) CancelCheckoutRequestDto request,
            @RequestHeader("X-User-Id") Long userId
    ) {
        CancelCheckoutRequestDto finalRequest =
                new CancelCheckoutRequestDto(
                        checkoutId,
                        request != null ? request.reason() : "USER_CANCELLED"
                );

        CheckoutSummaryResponseDto response =
                checkoutService.cancelCheckout(finalRequest, userId);

        return ResponseEntity.ok(response);
    }

    // 7.Expire checkout

    @PostMapping("/{checkoutId}/expire")
    public ResponseEntity<?> expireCheckout(
            @PathVariable Long checkoutId,
            @RequestBody(required = false) CancelCheckoutRequestDto request,
            @RequestHeader("X-User-Id") Long userId
    ) {

        checkoutService.expireCheckout(checkoutId);

        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{checkoutId}/payment-view")
    public CheckoutPaymentViewDto getCheckoutForPayment(
            @PathVariable Long checkoutId
    ) {
        return checkoutService.getCheckoutPaymentView(checkoutId);
    }

    // --------------------------------------------------
    // 2️⃣ Payment service → update checkout after payment
    // (OPTIONAL but recommended)
    // --------------------------------------------------

    @PutMapping("/{checkoutId}/payment-status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCheckoutAfterPayment(
            @PathVariable Long checkoutId,
            @RequestBody CheckoutPaymentUpdateRequestDto request
    ) {
        checkoutService.updateCheckoutAfterPayment(
                checkoutId,
                request.newStatus()
        );
    }

    @PutMapping("/{checkoutId}/delivery-type")
    public ResponseEntity<DeliveryTypeSelectionResponseDto> selectDeliveryType(
            @PathVariable Long checkoutId,
            @RequestBody SelectDeliveryTypeRequestDto request,
            @RequestHeader("X-User-Id") Long userId
    ) {

        SelectDeliveryTypeRequestDto finalRequest =
                new SelectDeliveryTypeRequestDto(
                        checkoutId,
                        request.deliveryType()
                );

        return ResponseEntity.ok(
                checkoutService.selectDeliveryType(
                        finalRequest,
                        userId
                )
        );
    }


}

package com.trademind.payment.controller;

import com.trademind.payment.dto.response.PaymentResponseDto;
import com.trademind.payment.dto.response.PaymentSummaryResponseDto;
import com.trademind.payment.dto.response.StripePaymentIntentResponseDto;
import com.trademind.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ---------------------------------------------------------
    // 1️⃣ Get payment details for a checkout
    // Used by frontend to render payment screen / retry
    // ---------------------------------------------------------

    @GetMapping("/checkout/{checkoutId}")
    public PaymentResponseDto getPaymentByCheckout(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long checkoutId
    ) {
        return paymentService.getPaymentByCheckoutId(
                checkoutId,
                userId
        );
    }

    // ---------------------------------------------------------
    // 2️⃣ Initiate Stripe payment (ONLINE only)
    // Frontend calls this before opening Stripe UI
    // ---------------------------------------------------------

    @PostMapping("/{paymentId}/initiate")
    @ResponseStatus(HttpStatus.CREATED)
    public StripePaymentIntentResponseDto initiateStripePayment(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long paymentId
    ) {
        return paymentService.initiateStripePayment(
                paymentId,
                userId
        );
    }

    // ---------------------------------------------------------
    // 3️⃣ Cancel payment explicitly (rare but correct)
    // Used if user backs out before paying
    // ---------------------------------------------------------

    @PostMapping("/{paymentId}/cancel")
    public PaymentSummaryResponseDto cancelPayment(
            @PathVariable Long paymentId,
            @RequestParam(defaultValue = "USER_CANCELLED") String reason
    ) {
        return paymentService.cancelPayment(
                paymentId,
                reason
        );
    }
}

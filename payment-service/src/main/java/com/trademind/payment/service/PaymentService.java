package com.trademind.payment.service;

import com.stripe.model.PaymentIntent;
import com.trademind.events.checkout.CheckoutConfirmedEvent;
import com.trademind.events.order.OrderCreationRequestedEvent;
import com.trademind.payment.dto.response.PaymentResponseDto;
import com.trademind.payment.dto.response.PaymentSummaryResponseDto;
import com.trademind.payment.dto.response.StripePaymentIntentResponseDto;

public interface PaymentService {

    /**
     * Triggered by Kafka when checkout.confirmed is published.
     * Creates a payment record (idempotent).
     */
    void createPaymentForOrder(OrderCreationRequestedEvent event);


    /**
     * Initiates Stripe PaymentIntent for ONLINE payments.
     * Called by frontend.
     */
    StripePaymentIntentResponseDto initiateStripePayment(
            Long paymentId,
            Long userId
    );

    /**
     * Stripe webhook success callback.
     */
    void handleStripePaymentSuccess(PaymentIntent intent);

    /**
     * Stripe webhook failure callback.
     */
    void handleStripePaymentFailure(PaymentIntent intent);

    /**
     * Fetch payment details (for UI / retry).
     */
    PaymentResponseDto getPaymentByCheckoutId(
            Long checkoutId,
            Long userId
    );

    /**
     * Cancel payment explicitly (rare but correct).
     */
    PaymentSummaryResponseDto cancelPayment(
            Long paymentId,
            String reason
    );


}

package com.trademind.payment.gateway.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.trademind.payment.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StripeClient {

    @Value("${stripe.webhook-secret:}")
    private String webhookSecret;

    /**
     * Create Stripe PaymentIntent
     */
    public PaymentIntent createPaymentIntent(
            BigDecimal amount,
            String currency,
            String idempotencyKey
    ) throws StripeException {

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount.multiply(BigDecimal.valueOf(100)).longValue()); // paise/cents
        params.put("currency", currency.toLowerCase());
        params.put("payment_method_types", java.util.List.of("card"));

        return PaymentIntent.create(params,
                com.stripe.net.RequestOptions.builder()
                        .setIdempotencyKey(idempotencyKey)
                        .build()
        );
    }

    /**
     * Verify Stripe webhook signature
     */
    public Event verifyWebhook(
            String payload,
            String sigHeader
    ) throws Exception {

        if (webhookSecret == null || webhookSecret.isBlank()) {
            throw new IllegalStateException("Stripe webhook secret not configured");
        }

        return Webhook.constructEvent(
                payload,
                sigHeader,
                webhookSecret
        );
    }
}

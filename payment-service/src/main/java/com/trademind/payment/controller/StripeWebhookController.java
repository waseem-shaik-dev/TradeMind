package com.trademind.payment.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.trademind.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            HttpServletRequest request,
            @RequestHeader("Stripe-Signature") String signature
    ) {

        String payload;

        try {
            payload = request.getReader()
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error("❌ Failed to read webhook payload", e);
            return ResponseEntity.badRequest().body("Invalid payload");
        }

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    signature,
                    webhookSecret
            );
        } catch (SignatureVerificationException e) {
            log.error("❌ Invalid Stripe signature", e);
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        log.info("✅ Stripe event received");
        log.info("➡ Event Type: {}", event.getType());
        log.info("➡ Event ID: {}", event.getId());
        log.info("➡ API Version: {}", event.getApiVersion());

        try {

            StripeObject stripeObject =
                    event.getDataObjectDeserializer().deserializeUnsafe();

            if (stripeObject == null) {
                log.error("❌ Failed to deserialize Stripe object");
                log.error("Raw payload: {}", payload);
                return ResponseEntity.ok("Webhook received but object null");
            }

            log.info("➡ Stripe Object Class: {}", stripeObject.getClass().getName());

            switch (event.getType()) {

                case "payment_intent.succeeded" -> {

                    if (stripeObject instanceof PaymentIntent intent) {

                        log.info("💰 PAYMENT SUCCESS");
                        log.info("Intent ID: {}", intent.getId());
                        log.info("Amount: {}", intent.getAmount());
                        log.info("Currency: {}", intent.getCurrency());
                        log.info("Status: {}", intent.getStatus());

                        paymentService.handleStripePaymentSuccess(intent);

                    } else {
                        log.error("❌ Event is not PaymentIntent. Class: {}",
                                stripeObject.getClass().getName());
                    }
                }

                case "payment_intent.payment_failed" -> {

                    if (stripeObject instanceof PaymentIntent intent) {

                        log.info("❌ PAYMENT FAILED");
                        log.info("Intent ID: {}", intent.getId());
                        log.info("Failure reason: {}", intent.getLastPaymentError());

                        paymentService.handleStripePaymentFailure(intent);

                    } else {
                        log.error("❌ Event is not PaymentIntent. Class: {}",
                                stripeObject.getClass().getName());
                    }
                }

                default -> {
                    log.info("ℹ Unhandled Stripe event type: {}", event.getType());
                }
            }

        } catch (Exception e) {
            log.error("🔥 Webhook processing error", e);
            log.error("Raw payload for debugging: {}", payload);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Webhook processing failed");
        }

        return ResponseEntity.ok("Webhook processed");
    }
}

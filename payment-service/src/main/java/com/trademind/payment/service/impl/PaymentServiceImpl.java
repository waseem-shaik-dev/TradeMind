package com.trademind.payment.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.trademind.events.order.OrderCreationRequestedEvent;
import com.trademind.payment.dto.response.*;
import com.trademind.payment.entity.PaymentTransaction;
import com.trademind.payment.enums.*;
import com.trademind.payment.feign.CheckoutClient;
import com.trademind.payment.gateway.stripe.StripeClient;
import com.trademind.payment.kafka.producer.PaymentEventProducer;
import com.trademind.payment.mapper.PaymentTransactionMapper;
import com.trademind.payment.mapper.StripePaymentMapper;
import com.trademind.payment.repository.PaymentTransactionRepository;
import com.trademind.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentTransactionRepository paymentRepo;
    private final PaymentTransactionMapper paymentMapper;
    private final StripePaymentMapper stripeMapper;
    private final StripeClient stripeClient;
    private final PaymentEventProducer eventProducer;
    private final CheckoutClient checkoutClient;

    @Value("${stripe.publishable-key}")
    private String stripePublishableKey;

    // ------------------------------------------------------------
    // 1️⃣ CREATE PAYMENT (Kafka: checkout.confirmed)
    // ------------------------------------------------------------

    @Override
    public void createPaymentForOrder(OrderCreationRequestedEvent event) {

        if (paymentRepo.existsByCheckoutId(event.checkoutId())) {
            return; // idempotent
        }

        PaymentTransaction payment =
                PaymentTransaction.builder()
                        .checkoutId(event.checkoutId())
                        .userId(event.userId())
                        .amount(event.grandTotal())
                        .currency(event.currency())
                        .paymentMethod(
                                PaymentMethod.valueOf(
                                        event.paymentMethod().name()
                                )
                        )
                        .paymentProvider(
                                event.paymentMethod().name().equals("COD")
                                        ? PaymentProvider.NONE
                                        : PaymentProvider.STRIPE
                        )
                        .status(
                                event.paymentMethod().name().equals("COD")
                                        ? PaymentStatus.PENDING
                                        : PaymentStatus.CREATED
                        )
                        .build();

        paymentRepo.save(payment);

        eventProducer.publishPaymentCreated(payment);
    }



    // ------------------------------------------------------------
    // 2️⃣ INITIATE STRIPE PAYMENT (Frontend)
    // ------------------------------------------------------------

    @Override
    public StripePaymentIntentResponseDto initiateStripePayment(
            Long paymentId,
            Long userId
    ) {
        PaymentTransaction payment =
                paymentRepo.findById(paymentId)
                        .orElseThrow(() ->
                                new IllegalStateException("Payment not found"));

        if (!payment.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized payment access");
        }

        if (payment.getPaymentMethod() != PaymentMethod.ONLINE) {
            throw new IllegalStateException("Not an online payment");
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Payment already completed");
        }

        try {
            PaymentIntent intent =
                    stripeClient.createPaymentIntent(
                            payment.getAmount(),
                            payment.getCurrency(),
                            UUID.randomUUID().toString()
                    );

            stripeMapper.mapFromPaymentIntent(payment, intent);
            paymentRepo.save(payment);

            eventProducer.publishPaymentInitiated(payment);

            return new StripePaymentIntentResponseDto(
                    payment.getId(),
                    payment.getProviderClientSecret(),
                    stripePublishableKey
            );

        } catch (StripeException ex) {
            ex.printStackTrace();
            stripeMapper.mapFailure(payment, ex.getMessage());
            paymentRepo.save(payment);

            eventProducer.publishPaymentFailure(payment);
            throw new IllegalStateException("Stripe initiation failed");
        }
    }

    // ------------------------------------------------------------
    // 3️⃣ STRIPE WEBHOOK: SUCCESS
    // ------------------------------------------------------------

    @Override
    public void handleStripePaymentSuccess(PaymentIntent intent) {

        PaymentTransaction payment =
                paymentRepo.findByProviderPaymentIntentId(intent.getId())
                        .orElseThrow();

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return; // idempotent
        }

        stripeMapper.mapSuccessFromIntent(payment, intent);
        paymentRepo.save(payment);

        eventProducer.publishPaymentSuccess(payment);

        eventProducer.publishOrderPaymentUpdated(
                payment.getCheckoutId(),
                com.trademind.events.order.PaymentStatus.PAID,
                intent.getId()
        );

    }


    // ------------------------------------------------------------
    // 4️⃣ STRIPE WEBHOOK: FAILURE
    // ------------------------------------------------------------

    @Override
    public void handleStripePaymentFailure(PaymentIntent intent) {

        PaymentTransaction payment =
                paymentRepo.findByProviderPaymentIntentId(intent.getId())
                        .orElseThrow();

        if (payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }

        stripeMapper.mapFailure(
                payment,
                intent.getLastPaymentError() != null
                        ? intent.getLastPaymentError().getMessage()
                        : "Stripe payment failed"
        );

        paymentRepo.save(payment);
        eventProducer.publishPaymentFailure(payment);

        eventProducer.publishOrderPaymentUpdated(
                payment.getCheckoutId(),
                com.trademind.events.order.PaymentStatus.FAILED,
                intent.getId()
        );
    }

    // ------------------------------------------------------------
    // 5️⃣ FETCH PAYMENT
    // ------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByCheckoutId(
            Long checkoutId,
            Long userId
    ) {
        PaymentTransaction payment =
                paymentRepo.findByCheckoutId(checkoutId)
                        .orElseThrow();

        if (!payment.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }

        return paymentMapper.toResponseDto(payment);
    }

    // ------------------------------------------------------------
    // 6️⃣ CANCEL PAYMENT
    // ------------------------------------------------------------

    @Override
    public PaymentSummaryResponseDto cancelPayment(
            Long paymentId,
            String reason
    ) {
        PaymentTransaction payment =
                paymentRepo.findById(paymentId)
                        .orElseThrow();

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Cannot cancel completed payment");
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setFailureReason(reason);

        paymentRepo.save(payment);
        eventProducer.publishPaymentCancelled(payment, reason);

        return paymentMapper.toSummaryDto(payment);
    }
}

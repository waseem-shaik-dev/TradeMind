package com.trademind.checkout.service.impl;

import com.trademind.checkout.dto.request.*;
import com.trademind.checkout.dto.response.*;
import com.trademind.checkout.entity.*;
import com.trademind.checkout.enums.*;
import com.trademind.checkout.feign.dto.cart.CartPriceSummaryDto;
import com.trademind.checkout.feign.dto.cart.CartResponseDto;
import com.trademind.checkout.feign.dto.cart.CartSourceDto;
import com.trademind.checkout.kafka.producer.CheckoutEventProducer;
import com.trademind.checkout.mapper.*;
import com.trademind.checkout.repository.CheckoutSessionRepository;
import com.trademind.checkout.service.CheckoutService;
import com.trademind.checkout.feign.CartClient;
import com.trademind.checkout.feign.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutSessionRepository checkoutSessionRepository;

    private final CartClient cartClient;
    private final UserClient userClient;

    private final CheckoutEventProducer eventProducer;

    private final CheckoutAggregateMapper aggregateMapper;
    private final CheckoutSessionMapper sessionMapper;

    private static final int CHECKOUT_EXPIRY_MINUTES = 15;

    // --------------------------------------------------------------------

    @Override
    public CheckoutSummaryResponseDto createCheckout(CreateCheckoutRequestDto request, Long userId) {

        CartResponseDto cart =
                cartClient.getCart(userId,request.cartId());

        if (!cart.validation().valid()) {
            throw new IllegalStateException(cart.validation().message());
        }

        CartSourceDto source = cart.source();
        CartPriceSummaryDto price = cart.priceSummary();

        CheckoutSession session = CheckoutSession.builder()
                .userId(cart.userId())
                .cartId(cart.cartId())
                .sourceId(source.sourceId())
                .sourceType(SourceType.valueOf(source.sourceType()))
                .status(CheckoutStatus.CREATED)
                .subtotalAmount(price.subTotal())
                .taxAmount(price.tax())
                .discountAmount(price.discount())
                .deliveryFee(BigDecimal.ZERO) // cart has no deliveryFee yet
                .grandTotal(price.grandTotal())
                .currency("INR")
                .expiresAt(LocalDateTime.now().plusMinutes(CHECKOUT_EXPIRY_MINUTES))
                .build();

        cart.items().forEach(ci -> {
            session.addItem(
                    CheckoutItem.builder()
                            .productId(ci.productId())
                            .productName(ci.productName())
                            .sku(ci.sku())
                            .unitPrice(ci.unitPrice())
                            .quantity(ci.quantity())
                            .totalPrice(ci.totalPrice())
                            .build()
            );
        });

        checkoutSessionRepository.save(session);

        return sessionMapper.toSummaryResponse(session);
    }


    // --------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public CheckoutResponseDto getCheckout(Long checkoutId, Long userId) {

        CheckoutSession session = checkoutSessionRepository
                .findByIdAndUserId(checkoutId, userId)
                .orElseThrow(() -> new IllegalStateException("Checkout not found"));

        return aggregateMapper.toCheckoutResponse(session);
    }

    // --------------------------------------------------------------------

    @Override
    public CheckoutSummaryResponseDto selectAddress(
            SelectAddressRequestDto request,
            Long userId
    ) {
        CheckoutSession session = getActiveCheckout(request.checkoutId(), userId);

        var address = userClient.getMyAddressById(userId, request.addressId());

        CheckoutAddressSnapshot snapshot = CheckoutAddressSnapshot.builder()
                .fullName(address.fullName())
                .phone(address.phone())
                .addressLine1(address.line1())
                .addressLine2(address.line2())
                .city(address.city())
                .state(address.state())
                .postalCode(address.pincode())
                .country(address.country())
                .build();


        session.setAddressSnapshot(snapshot);
        session.setStatus(CheckoutStatus.ADDRESS_SELECTED);

        return sessionMapper.toSummaryResponse(session);
    }

    // --------------------------------------------------------------------

    @Override
    public CheckoutSummaryResponseDto selectPaymentMethod(
            SelectPaymentMethodRequestDto request,
            Long userId
    ) {
        CheckoutSession session = getActiveCheckout(request.checkoutId(), userId);

        CheckoutPaymentSnapshot payment = CheckoutPaymentSnapshot.builder()
                .paymentMethod(request.paymentMethod())
                .paymentProvider(
                        request.paymentMethod() == PaymentMethod.COD
                                ? "NONE"
                                : "RAZORPAY"
                )
                .amount(session.getGrandTotal())
                .currency(session.getCurrency())
                .status(PaymentStatus.INITIATED)
                .build();

        session.setPaymentSnapshot(payment);
        session.setStatus(CheckoutStatus.PAYMENT_SELECTED);

        return sessionMapper.toSummaryResponse(session);
    }

    // --------------------------------------------------------------------

    @Override
    public CheckoutSummaryResponseDto confirmCheckout(Long checkoutId, Long userId) {

        CheckoutSession session = getActiveCheckout(checkoutId, userId);

        validateCheckoutCompleteness(session);

        session.setStatus(CheckoutStatus.RESERVED);

        // 1️⃣ Reserve inventory
        eventProducer.publishInventoryReserveEvent(session);

        // 2️⃣ Notify downstream (payment/order)
        eventProducer.publishCheckoutConfirmedEvent(session);

        return sessionMapper.toSummaryResponse(session);
    }

    // --------------------------------------------------------------------

    @Override
    public CheckoutSummaryResponseDto cancelCheckout(
            CancelCheckoutRequestDto request,
            Long userId
    ) {
        CheckoutSession session = checkoutSessionRepository
                .findByIdAndUserId(request.checkoutId(), userId)
                .orElseThrow(() -> new IllegalStateException("Checkout not found"));

        if (session.getStatus() == CheckoutStatus.CANCELLED ||
                session.getStatus() == CheckoutStatus.EXPIRED) {
            return sessionMapper.toSummaryResponse(session);
        }

        session.setStatus(CheckoutStatus.CANCELLED);

        eventProducer.publishInventoryReleaseEvent(session, request.reason());
        eventProducer.publishCheckoutCancelledEvent(session, request.reason());

        return sessionMapper.toSummaryResponse(session);
    }

    // --------------------------------------------------------------------

    @Override
    public void expireCheckout(Long checkoutId) {

        checkoutSessionRepository.findById(checkoutId).ifPresent(session -> {

            if (session.getStatus() == CheckoutStatus.CONFIRMED ||
                    session.getStatus() == CheckoutStatus.CANCELLED) {
                return;
            }

            session.setStatus(CheckoutStatus.EXPIRED);

            eventProducer.publishInventoryReleaseEvent(
                    session,
                    "CHECKOUT_EXPIRED"
            );

            eventProducer.publishCheckoutCancelledEvent(
                    session,
                    "CHECKOUT_EXPIRED"
            );
        });
    }

    // --------------------------------------------------------------------
    // ----------------------- INTERNAL HELPERS ----------------------------
    // --------------------------------------------------------------------

    private CheckoutSession getActiveCheckout(Long checkoutId, Long userId) {

        CheckoutSession session = checkoutSessionRepository
                .findByIdAndUserId(checkoutId, userId)
                .orElseThrow(() -> new IllegalStateException("Checkout not found"));

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            expireCheckout(session.getId());
            throw new IllegalStateException("Checkout expired");
        }

        if (session.getStatus() == CheckoutStatus.CANCELLED ||
                session.getStatus() == CheckoutStatus.EXPIRED) {
            throw new IllegalStateException("Checkout not active");
        }

        return session;
    }

    private void validateCheckoutCompleteness(CheckoutSession session) {

        if (session.getAddressSnapshot() == null) {
            throw new IllegalStateException("Address not selected");
        }

        if (session.getPaymentSnapshot() == null) {
            throw new IllegalStateException("Payment method not selected");
        }
    }
}

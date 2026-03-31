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
    public CheckoutSummaryResponseDto createCheckout(CreateCheckoutRequestDto request, Long userId, String userRole) {

        CartResponseDto cart =
                cartClient.getCart(userId,request.cartId());

        if (!cart.validation().valid()) {
            throw new IllegalStateException(cart.validation().message());
        }

        CartSourceDto source = cart.source();
        CartPriceSummaryDto price = cart.priceSummary();



        CheckoutSession session = CheckoutSession.builder()
                .userId(cart.userId())
                .userEmail(request.userEmail())
                .cartId(cart.cartId())
                .buyerType(BuyerType.valueOf(userRole))
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
                            .imageUrl(ci.primaryImage())
                            .unitPrice(ci.unitPrice())
                            .quantity(ci.quantity())
                            .totalPrice(ci.totalPrice())
                            .build()
            );
        });

        // 🔒 Lock cart immediately to prevent modifications
        cartClient.lockCartForCheckout(
                userId,
                cart.cartId()
        );


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

        CheckoutAddressSnapshot snapshot = session.getAddressSnapshot();

        if (snapshot == null) {
            snapshot = new CheckoutAddressSnapshot();
            snapshot.setCheckoutSession(session); // VERY IMPORTANT
        }

        snapshot.setFullName(address.fullName());
        snapshot.setPhone(address.phone());
        snapshot.setAddressLine1(address.line1());
        snapshot.setAddressLine2(address.line2());
        snapshot.setCity(address.city());
        snapshot.setState(address.state());
        snapshot.setPostalCode(address.pincode());
        snapshot.setCountry(address.country());

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

        PaymentMethod paymentMethod= request.paymentMethod();

        boolean isCOD = paymentMethod.equals(PaymentMethod.COD);

        CheckoutPaymentSnapshot payment = session.getPaymentSnapshot();

        if(payment == null){
            payment = new CheckoutPaymentSnapshot();
            payment.setCheckoutSession(session);
        }

        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentProvider(request.paymentMethod() == PaymentMethod.COD
                ? "NONE"
                : "STRIPE");
        payment.setAmount(session.getGrandTotal());
        payment.setCurrency(session.getCurrency());
        payment.setStatus(isCOD?PaymentStatus.PENDING:PaymentStatus.INITIATED);


        session.setPaymentSnapshot(payment);
        session.setStatus(CheckoutStatus.PAYMENT_SELECTED);

        return sessionMapper.toSummaryResponse(session);
    }

    @Override
    public DeliveryTypeSelectionResponseDto selectDeliveryType(
            SelectDeliveryTypeRequestDto request,
            Long userId
    ) {
        CheckoutSession session =
                getActiveCheckout(request.checkoutId(), userId);

        session.setDeliveryType(request.deliveryType());

        // Example logic
        if (request.deliveryType() == DeliveryType.HOME_DELIVERY) {
            session.setDeliveryFee(BigDecimal.valueOf(40)); // example
        } else {
            session.setDeliveryFee(BigDecimal.ZERO);
        }

        session.setGrandTotal(
                session.getSubtotalAmount()
                        .add(session.getTaxAmount())
                        .subtract(session.getDiscountAmount())
                        .add(session.getDeliveryFee())
        );

        session.setStatus(CheckoutStatus.DELIVERY_SELECTED);

        return new DeliveryTypeSelectionResponseDto(
                session.getId(),
                session.getDeliveryType(),
                session.getDeliveryFee(),
                session.getGrandTotal(),
                session.getStatus()
        );
    }


    // --------------------------------------------------------------------

    @Override
    public CheckoutSummaryResponseDto confirmCheckout(Long checkoutId, Long userId) {

        CheckoutSession session = getActiveCheckout(checkoutId, userId);

        validateCheckoutCompleteness(session);

        if (session.getStatus() == CheckoutStatus.RESERVED ||
                session.getStatus() == CheckoutStatus.CONFIRMED) {
            return sessionMapper.toSummaryResponse(session);
        }

        if (session.getStatus() != CheckoutStatus.PAYMENT_SELECTED) {
            throw new IllegalStateException("Checkout not ready for confirmation");
        }

        session.setStatus(CheckoutStatus.RESERVED);

        // Reserve inventory
        eventProducer.publishInventoryReserveEvent(session);

//        // Notify payment / order services
//        eventProducer.publishCheckoutConfirmedEvent(session);
//

        System.out.println("printing payment snapshot of checkoutSession "+session.getPaymentSnapshot());

        eventProducer.publishOrderCreationRequestedEvent(session);


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

//            eventProducer.publishInventoryReleaseEvent(
//                    session,
//                    "CHECKOUT_EXPIRED"
//            );
//
//            eventProducer.publishCheckoutCancelledEvent(
//                    session,
//                    "CHECKOUT_EXPIRED"
//            );

            cartClient.unlockCartForCheckout(session.getUserId(), session.getCartId());

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

        if(session.getDeliveryType()==null){
            throw new IllegalStateException("Delivery type is not selected");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CheckoutPaymentViewDto getCheckoutPaymentView(Long checkoutId) {

        CheckoutSession session =
                checkoutSessionRepository.findById(checkoutId)
                        .orElseThrow(() ->
                                new IllegalStateException("Checkout not found"));

        if (session.getStatus() == CheckoutStatus.CANCELLED ||
                session.getStatus() == CheckoutStatus.EXPIRED) {
            throw new IllegalStateException("Checkout not payable");
        }

        return new CheckoutPaymentViewDto(
                session.getId(),
                session.getUserId(),
                session.getGrandTotal(),
                session.getCurrency(),
                session.getStatus().name()
        );
    }

    @Override
    @Transactional
    public void updateCheckoutAfterPayment(
            Long checkoutId,
            String newStatus
    ) {
        CheckoutSession session =
                checkoutSessionRepository.findById(checkoutId)
                        .orElseThrow(() ->
                                new IllegalStateException("Checkout not found"));

        CheckoutStatus status =
                CheckoutStatus.valueOf(newStatus);

        switch (status) {

            case CONFIRMED -> {
                if (session.getStatus() != CheckoutStatus.RESERVED) {
                    throw new IllegalStateException(
                            "Checkout not ready to be confirmed"
                    );
                }
                session.setStatus(CheckoutStatus.CONFIRMED);
            }

            case CANCELLED -> {
                if (session.getStatus() == CheckoutStatus.CONFIRMED) {
                    throw new IllegalStateException(
                            "Cannot cancel confirmed checkout"
                    );
                }
                session.setStatus(CheckoutStatus.CANCELLED);
            }

            default -> throw new IllegalArgumentException(
                    "Unsupported checkout status update"
            );
        }
    }





}

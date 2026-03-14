package com.trademind.order.mapper;

import com.trademind.events.order.OrderCreationRequestedEvent;
import com.trademind.events.order.OrderItemDto;
import com.trademind.order.dto.response.*;
import com.trademind.order.dto.view.*;
import com.trademind.order.entity.*;
import com.trademind.order.enums.*;
import com.trademind.order.service.validator.OrderActionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderActionResolver actionResolver;


    // ============================================================
    // 1️⃣ Event → Entity (Kafka Consumer)
    // ============================================================

    public Order fromOrderCreationEvent(OrderCreationRequestedEvent event) {

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .checkoutId(event.checkoutId())
                .cartId(event.cartId())
                .userId(event.userId())
                .buyerType(BuyerType.valueOf(event.buyerType()))
                .sourceId(event.sourceId())
                .sourceType(SourceType.valueOf(event.sourceType()))
                .deliveryType(DeliveryType.valueOf(event.deliveryType().name()))
                .orderStatus(
                event.paymentStatus().name().equals("PAID")
                        ? OrderStatus.AWAITING_ACCEPTANCE
                        : OrderStatus.CREATED
        )

                .paymentStatus(
                        PaymentStatus.valueOf(event.paymentStatus().name())
                )
                .paymentMethod(
                        PaymentMethod.valueOf(event.paymentMethod().name())
                )
                .subtotalAmount(event.subtotalAmount())
                .taxAmount(event.taxAmount())
                .discountAmount(event.discountAmount())
                .deliveryFee(event.deliveryFee())
                .grandTotal(event.grandTotal())
                .currency(event.currency())
                .build();

        // Address
        OrderAddressSnapshot addressSnapshot = OrderAddressSnapshot.builder()
                .fullName(event.address().fullName())
                .phone(event.address().phone())
                .addressLine1(event.address().addressLine1())
                .addressLine2(event.address().addressLine2())
                .city(event.address().city())
                .state(event.address().state())
                .postalCode(event.address().postalCode())
                .country(event.address().country())
                .build();

        order.setAddressSnapshot(addressSnapshot);

        // Items
        event.items().forEach(itemDto ->
                order.addLineItem(mapToLineItem(itemDto))
        );

        return order;
    }

    private OrderLineItem mapToLineItem(OrderItemDto dto) {
        return OrderLineItem.builder()
                .productId(dto.productId())
                .productName(dto.productName())
                .sku(dto.sku())
                .imageUrl(dto.imageUrl())
                .unitPrice(dto.unitPrice())
                .quantity(dto.quantity())
                .totalPrice(dto.totalPrice())
                .build();
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // ============================================================
    // 2️⃣ Entity → Summary DTO
    // ============================================================

    public OrderSummaryResponseDto toSummaryDto(Order order) {

        return new OrderSummaryResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getUserId(),
                order.getSourceId(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getGrandTotal(),
                order.getCurrency(),
                order.getCreatedAt()
        );
    }

    // ============================================================
    // 3️⃣ Entity → Detail DTO
    // ============================================================

    public OrderDetailResponseDto toDetailDto(Order order) {

        return new OrderDetailResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getCheckoutId(),
                order.getCartId(),
                order.getUserId(),
                order.getSourceId(),
                order.getSourceType(),
                order.getDeliveryType(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getPaymentMethod(),
                order.getSubtotalAmount(),
                order.getTaxAmount(),
                order.getDiscountAmount(),
                order.getDeliveryFee(),
                order.getGrandTotal(),
                order.getCurrency(),
                mapAddress(order.getAddressSnapshot()),
                mapLineItems(order.getLineItems()),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private OrderAddressDto mapAddress(OrderAddressSnapshot snapshot) {

        if (snapshot == null) return null;

        return new OrderAddressDto(
                snapshot.getFullName(),
                snapshot.getPhone(),
                snapshot.getAddressLine1(),
                snapshot.getAddressLine2(),
                snapshot.getCity(),
                snapshot.getState(),
                snapshot.getPostalCode(),
                snapshot.getCountry()
        );
    }

    private List<OrderLineItemDto> mapLineItems(List<OrderLineItem> items) {
        return items.stream()
                .map(i -> new OrderLineItemDto(
                        i.getProductId(),
                        i.getProductName(),
                        i.getSku(),
                        i.getImageUrl(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getTotalPrice()
                ))
                .collect(Collectors.toList());
    }

    // ============================================================
    // 4️⃣ Role-Specific View DTOs
    // ============================================================

    public CustomerOrderViewDto toCustomerView(Order order) {

        return new CustomerOrderViewDto(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getGrandTotal(),
                order.getCurrency(),
                order.getCreatedAt()
        );
    }

    public MerchantOrderViewDto toMerchantView(Order order) {

        return new MerchantOrderViewDto(
                order.getId(),
                order.getOrderNumber(),
                order.getUserId(),
                order.getDeliveryType(),
                order.getOrderStatus(),
                order.getGrandTotal(),
                order.getCreatedAt()
        );
    }

    public RetailerOrderViewDto toRetailerView(Order order) {

        return new RetailerOrderViewDto(
                order.getId(),
                order.getOrderNumber(),
                order.getUserId(),
                order.getDeliveryType(),
                order.getOrderStatus(),
                order.getGrandTotal(),
                order.getCreatedAt()
        );
    }

    public AdminOrderViewDto toAdminView(Order order) {

        return new AdminOrderViewDto(
                order.getId(),
                order.getOrderNumber(),
                order.getUserId(),
                order.getSourceId(),
                order.getSourceType(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getGrandTotal(),
                order.getCreatedAt()
        );
    }

    // ============================================================
    // 5️⃣ Status Update Response
    // ============================================================

    public OrderStatusUpdateResponseDto toStatusUpdateResponse(
            Order order,
            String message
    ) {

        return new OrderStatusUpdateResponseDto(
                order.getId(),
                order.getOrderStatus(),
                message,
                order.getUpdatedAt()
        );
    }

    public OrderFullViewDto toFullView(
            Order order,
            OrderActor actor
    ) {

        return new OrderFullViewDto(
                order.getId(),
                order.getOrderNumber(),
                order.getUserId(),
                order.getSourceId(),
                order.getSourceType(),
                order.getDeliveryType(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getPaymentMethod(),
                mapPricing(order),
                mapAddress(order.getAddressSnapshot()),
                mapLineItems(order.getLineItems()),
                actionResolver.resolveActions(order, actor),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }


    private OrderPricingDto mapPricing(Order order) {

        return new OrderPricingDto(
                order.getSubtotalAmount(),
                order.getTaxAmount(),
                order.getDiscountAmount(),
                order.getDeliveryFee(),
                order.getGrandTotal(),
                order.getCurrency()
        );
    }


}

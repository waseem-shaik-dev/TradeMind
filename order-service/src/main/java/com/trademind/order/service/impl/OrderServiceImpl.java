package com.trademind.order.service.impl;

import com.trademind.events.notification.enums.NotificationType;
import com.trademind.notification.sdk.annotation.Notify;
import com.trademind.order.dto.request.*;
import com.trademind.order.dto.response.*;
import com.trademind.order.dto.view.*;
import com.trademind.order.entity.Order;
import com.trademind.order.enums.*;
import com.trademind.order.feign.InventoryClient;
import com.trademind.order.kafka.producer.BillingEventProducer;
import com.trademind.order.kafka.producer.InventoryEventProducer;
import com.trademind.order.kafka.producer.OrderEventProducer;
import com.trademind.order.mapper.OrderMapper;
import com.trademind.order.repository.OrderRepository;
import com.trademind.order.service.OrderService;
import com.trademind.order.service.validator.OrderStateMachineValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer eventProducer;
    private final OrderStateMachineValidator validator;
    private final InventoryClient inventoryClient;
    private final InventoryEventProducer inventoryEventProducer;
    private final BillingEventProducer billingEventProducer;


    // ============================================================
    // CUSTOMER
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<OrderFullViewDto> getCustomerOrders(
            Long userId,
            Pageable pageable
    ) {
        return orderRepository.findByUserId(userId, pageable)
                .map(order ->orderMapper.toFullView(
                        order,
                        OrderActor.CUSTOMER
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderFullViewDto getCustomerOrderDetail(
            Long orderId,
            Long userId
    ) {
        Order order = orderRepository.findDetailedById(orderId)
                .orElseThrow();

        if (!order.getUserId().equals(userId))
            throw new IllegalStateException("Unauthorized");

        return orderMapper.toFullView(
                order,
                OrderActor.CUSTOMER
                );
    }


    @Override
    public OrderStatusUpdateResponseDto cancelOrderByCustomer(
            Long orderId,
            Long userId,
            CancelOrderRequestDto request
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        validator.validateTransition(
                order,
                OrderAction.CANCEL,
                OrderActor.CUSTOMER
        );

        switch (order.getPaymentStatus()) {

            case PAID ->
                    inventoryClient.cancelCommittedStock(order.getCheckoutId());

            case PENDING ->
                    inventoryEventProducer.publishInventoryRelease(order.getCheckoutId());

            default -> {}
        }



        order.setOrderStatus(OrderStatus.CANCELLED);

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            order.setPaymentStatus(PaymentStatus.REFUND_INITIATED);
        }


        orderRepository.save(order);

       // eventProducer.publishOrderCancelled(order, request.reason());



        return orderMapper.toStatusUpdateResponse(
                order,
                "Order cancelled successfully"
        );
    }

    @Override
    public OrderStatusUpdateResponseDto cancelOrderByRetailer(
            Long orderId,
            Long userId,
            CancelOrderRequestDto request
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        validator.validateTransition(
                order,
                OrderAction.CANCEL,
                OrderActor.RETAILER
        );

        switch (order.getPaymentStatus()) {

            case PAID ->
                    inventoryClient.cancelCommittedStock(order.getCheckoutId());

            case PENDING ->
                    inventoryEventProducer.publishInventoryRelease(order.getCheckoutId());

            default -> {}
        }



        order.setOrderStatus(OrderStatus.CANCELLED);

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            order.setPaymentStatus(PaymentStatus.REFUND_INITIATED);
        }


        orderRepository.save(order);

     //   eventProducer.publishOrderCancelled(order, request.reason());



        return orderMapper.toStatusUpdateResponse(
                order,
                "Order cancelled successfully"
        );
    }

    @Override
    public OrderDetailResponseDto getDetailedOrderById(Long orderId) {
        return orderRepository.findDetailedById(orderId).map(
                orderMapper::toDetailDto
        ).orElseThrow(()-> new RuntimeException("Order with Id "+orderId+" not found"));
    }


    // ============================================================
    // MERCHANT / RETAILER
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<OrderFullViewDto> getRetailerOrders(
            Long sourceId,
            Pageable pageable
    ) {
        return orderRepository
                .findBySourceIdAndSourceType(
                        sourceId,
                        SourceType.RETAILER,
                        pageable
                )
                .map(order -> orderMapper.toFullView(
                        order,
                        OrderActor.RETAILER
                ));

    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderFullViewDto> getRetailerMyOrders(
            Long retailerId,
            Pageable pageable
    ) {

        return orderRepository
                .findByUserIdAndBuyerType(
                        retailerId,
                        BuyerType.RETAILER,
                        pageable
                )
                .map(order -> orderMapper.toFullView(
                        order,
                        OrderActor.RETAILER
                ));
    }


    @Override
    @Transactional(readOnly = true)
    public Page<OrderFullViewDto> getMerchantOrders(
            Long sourceId,
            Pageable pageable
    ) {
        return orderRepository
                .findBySourceIdAndSourceType(
                        sourceId,
                        SourceType.MERCHANT,
                        pageable
                )
                .map(order -> orderMapper.toFullView(
                        order,
                        OrderActor.MERCHANT
                ));

    }

    @Override
    public OrderStatusUpdateResponseDto updateOrderStatus(
            Long orderId,
            Long actorId,
            OrderStatusUpdateRequestDto request,
            String actorRole
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        OrderActor actor = OrderActor.valueOf(actorRole);

        // Terminal state protection
        if (order.getOrderStatus() == OrderStatus.DELIVERED ||
                order.getOrderStatus() == OrderStatus.CANCELLED ||
                order.getOrderStatus() == OrderStatus.REJECTED ||
                order.getOrderStatus() == OrderStatus.FAILED) {

            throw new IllegalStateException("Order already finalized");
        }

        // Ownership validation
        if (actor == OrderActor.MERCHANT || actor == OrderActor.RETAILER) {
            if (!order.getSourceId().equals(actorId)) {
                throw new IllegalStateException("Unauthorized order access");
            }
        }

        validator.validateTransition(order, request.action(), actor);

        switch (request.action()) {

            case ACCEPT -> {
                order.setOrderStatus(OrderStatus.ACCEPTED);
            }

            case REJECT -> {
                order.setOrderStatus(OrderStatus.REJECTED);

                if (order.getPaymentStatus() == PaymentStatus.PAID) {
                    order.setPaymentStatus(PaymentStatus.REFUND_INITIATED);
                }
            }

            case MARK_PROCESSING -> {
                order.setOrderStatus(OrderStatus.PROCESSING);
            }

            case MARK_PACKED -> {
                order.setOrderStatus(OrderStatus.PACKED);
            }

            case MARK_OUT_FOR_DELIVERY -> {
                order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
            }

            case MARK_DELIVERED -> {
                order.setOrderStatus(OrderStatus.DELIVERED);
            }

            default -> throw new IllegalArgumentException("Invalid action");
        }

        orderRepository.save(order);

        return orderMapper.toStatusUpdateResponse(
                order,
                "Order updated successfully"
        );
    }


    @Override
    public OrderStatusUpdateResponseDto markCodPaymentReceived(
            Long orderId,
            Long actorId,
            String actorRole
    )
    {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Payment already marked as received");
        }

        if (order.getPaymentMethod() != PaymentMethod.COD) {
            throw new IllegalStateException("Not a COD order");
        }


        OrderActor actor = OrderActor.valueOf(actorRole);

        // 🔒 Ownership validation
        if (!order.getSourceId().equals(actorId)) {
            throw new IllegalStateException("Unauthorized order access");
        }

        validator.validateTransition(
                order,
                OrderAction.MARK_PAYMENT_RECEIVED_COD,
                actor
        );

        order.setPaymentStatus(PaymentStatus.PAID);

        // 🔥 TRIGGER BILLING
        billingEventProducer.publishBillingEvent(order);


        return orderMapper.toStatusUpdateResponse(
                order,
                "COD payment marked as received"
        );

    }

    // ============================================================
    // ADMIN
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<OrderFullViewDto> getAllOrders(Pageable pageable) {

        return orderRepository.findAll(pageable)
                .map(order -> orderMapper.toFullView(
                        order,
                        OrderActor.ADMIN
                ));

    }

    @Notify(
            type = NotificationType.ORDER_CREATED,
            recipientExpression = "#order.userEmail",
            dataExpression = "{'orderId': #order.id, 'amount': #order.grandTotal}"
    )
    public Order saveOrderWithNotification(Order order) {


        return orderRepository.save(order);
    }
}

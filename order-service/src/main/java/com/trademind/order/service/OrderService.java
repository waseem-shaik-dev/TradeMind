package com.trademind.order.service;

import com.trademind.order.dto.request.*;
import com.trademind.order.dto.response.*;
import com.trademind.order.dto.view.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    // ---------------- CUSTOMER ----------------

    Page<OrderFullViewDto> getCustomerOrders(
            Long userId,
            Pageable pageable
    );

    OrderFullViewDto getCustomerOrderDetail(
            Long orderId,
            Long userId
    );

    OrderStatusUpdateResponseDto cancelOrderByCustomer(
            Long orderId,
            Long userId,
            CancelOrderRequestDto request
    );

    // ---------------- MERCHANT / RETAILER ----------------

    Page<OrderFullViewDto> getRetailerOrders(
            Long sourceId,
            Pageable pageable
    );

    Page<OrderFullViewDto> getRetailerMyOrders(
            Long retailerId,
            Pageable pageable
    );



    Page<OrderFullViewDto> getMerchantOrders(
            Long sourceId,
            Pageable pageable
    );

    OrderStatusUpdateResponseDto updateOrderStatus(
            Long orderId,
            Long actorId,
            OrderStatusUpdateRequestDto request,
            String actorRole
    );

    public OrderStatusUpdateResponseDto markCodPaymentReceived(
            Long orderId,
            Long actorId,
            String actorRole
    );


    // ---------------- ADMIN ----------------

    Page<OrderFullViewDto> getAllOrders(Pageable pageable);

    OrderStatusUpdateResponseDto cancelOrderByRetailer(
            Long orderId,
            Long userId,
            CancelOrderRequestDto request
    );


    OrderDetailResponseDto getDetailedOrderById(Long orderId);
}

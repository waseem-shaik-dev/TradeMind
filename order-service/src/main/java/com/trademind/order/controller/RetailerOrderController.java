package com.trademind.order.controller;

import com.trademind.order.dto.request.CancelOrderRequestDto;
import com.trademind.order.dto.request.CodPaymentReceivedRequestDto;
import com.trademind.order.dto.request.OrderStatusUpdateRequestDto;
import com.trademind.order.dto.response.OrderDetailResponseDto;
import com.trademind.order.dto.response.OrderStatusUpdateResponseDto;
import com.trademind.order.dto.view.OrderFullViewDto;
import com.trademind.order.dto.view.RetailerOrderViewDto;
import com.trademind.order.enums.OrderActor;
import com.trademind.order.mapper.OrderMapper;
import com.trademind.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/retailer")
@RequiredArgsConstructor
public class RetailerOrderController {

    private final OrderService orderService;

    @GetMapping
    public Page<OrderFullViewDto> getRetailerOrders(
            @RequestHeader("X-User-Id") Long sourceId,
            Pageable pageable
    ) {
        return orderService.getRetailerOrders(sourceId,pageable);
    }

    @GetMapping("/my-orders")
    public Page<OrderFullViewDto> getRetailerMyOrders(
            @RequestHeader("X-User-Id") Long retailerId,
            Pageable pageable
    ) {
        return orderService.getRetailerMyOrders(
                retailerId,
                pageable
        );
    }


    @PutMapping("/{orderId}/status")
    public OrderStatusUpdateResponseDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long retailerId,
            @Valid @RequestBody OrderStatusUpdateRequestDto request
    ) {
        return orderService.updateOrderStatus(
                orderId,
                retailerId,
                request,
                "RETAILER"
        );
    }

    @PutMapping("/{orderId}/cod-payment-received")
    public OrderStatusUpdateResponseDto markCodPaymentReceived(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long retailerId
    ) {
        return orderService.markCodPaymentReceived(
                orderId,
                retailerId,
                "RETAILER"
        );
    }

    @PutMapping("/{orderId}/cancel")
    public OrderStatusUpdateResponseDto cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CancelOrderRequestDto request
    ) {
        return orderService.cancelOrderByRetailer(orderId, userId, request);
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponseDto getDetailedOrderById(@PathVariable Long orderId){
        return orderService.getDetailedOrderById(orderId);
    }

}

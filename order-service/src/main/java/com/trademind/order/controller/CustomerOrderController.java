package com.trademind.order.controller;

import com.trademind.order.dto.request.CancelOrderRequestDto;
import com.trademind.order.dto.response.OrderDetailResponseDto;
import com.trademind.order.dto.response.OrderStatusUpdateResponseDto;
import com.trademind.order.dto.view.CustomerOrderViewDto;
import com.trademind.order.dto.view.OrderFullViewDto;
import com.trademind.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/customer")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final OrderService orderService;

    @GetMapping
    public Page<OrderFullViewDto> getMyOrders(
            @RequestHeader("X-User-Id") Long userId,
            Pageable pageable
    ) {
        return orderService.getCustomerOrders(userId, pageable);
    }

    @GetMapping("/{orderId}/fullView")
    public OrderFullViewDto getOrderDetail(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        return orderService.getCustomerOrderDetail(orderId, userId);
    }

    @PutMapping("/{orderId}/cancel")
    public OrderStatusUpdateResponseDto cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CancelOrderRequestDto request
    ) {
        return orderService.cancelOrderByCustomer(orderId, userId, request);
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponseDto getDetailedOrderById(@PathVariable Long orderId){
        return orderService.getDetailedOrderById(orderId);
    }

}

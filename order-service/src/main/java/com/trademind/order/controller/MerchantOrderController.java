package com.trademind.order.controller;


import com.trademind.order.dto.request.OrderStatusUpdateRequestDto;
import com.trademind.order.dto.response.OrderDetailResponseDto;
import com.trademind.order.dto.response.OrderStatusUpdateResponseDto;
import com.trademind.order.dto.response.OrderTrackingStepDto;
import com.trademind.order.dto.view.OrderFullViewDto;
import com.trademind.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/merchant")
@RequiredArgsConstructor
public class MerchantOrderController {

    private final OrderService orderService;

    @GetMapping
    public Page<OrderFullViewDto> getMerchantOrders(
            @RequestHeader("X-User-Id") Long sourceId,
            Pageable pageable
    ) {
        return orderService.getMerchantOrders(sourceId, pageable);
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponseDto getDetailedOrderById(@PathVariable Long orderId){
        return orderService.getDetailedOrderById(orderId);
    }

    @PutMapping("/{orderId}/status")
    public OrderStatusUpdateResponseDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long merchantId,
            @Valid @RequestBody OrderStatusUpdateRequestDto request
    ) {
        return orderService.updateOrderStatus(
                orderId,
                merchantId,
                request,
                "MERCHANT"
        );
    }

    @PutMapping("/{orderId}/cod-payment-received")
    public OrderStatusUpdateResponseDto markCodPaymentReceived(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long merchantId
    ) {
        return orderService.markCodPaymentReceived(
                orderId,
                merchantId,
                "MERCHANT"
        );
    }

    @GetMapping("/{orderId}/tracking")
    public List<OrderTrackingStepDto> getTracking(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        return orderService.getTrackingSteps(orderId, userId, "MERCHANT");
    }
}

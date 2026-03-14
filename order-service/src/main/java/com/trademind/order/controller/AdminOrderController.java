package com.trademind.order.controller;

import com.trademind.order.dto.request.CancelOrderRequestDto;
import com.trademind.order.dto.response.OrderDetailResponseDto;
import com.trademind.order.dto.response.OrderStatusUpdateResponseDto;
import com.trademind.order.dto.view.AdminOrderViewDto;
import com.trademind.order.dto.view.OrderFullViewDto;
import com.trademind.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public Page<OrderFullViewDto> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PutMapping("/{orderId}/force-cancel")
    public OrderStatusUpdateResponseDto forceCancel(
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderRequestDto request
    ) {
        return orderService.updateOrderStatus(
                orderId,
                0L,
                new com.trademind.order.dto.request.OrderStatusUpdateRequestDto(
                        com.trademind.order.enums.OrderAction.CANCEL,
                        request.reason()
                ),
                "ADMIN"
        );
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponseDto getDetailedOrderById(@PathVariable Long orderId){
        return orderService.getDetailedOrderById(orderId);
    }

}

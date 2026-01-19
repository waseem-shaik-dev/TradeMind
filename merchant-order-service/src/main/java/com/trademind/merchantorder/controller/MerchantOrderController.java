package com.trademind.merchantorder.controller;

import com.trademind.merchantorder.dto.CreateMerchantOrderRequest;
import com.trademind.merchantorder.entity.MerchantOrder;
import com.trademind.merchantorder.service.MerchantOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant-orders")
@RequiredArgsConstructor
public class MerchantOrderController {

    private final MerchantOrderService orderService;

    @PostMapping
    public MerchantOrder create(@RequestBody CreateMerchantOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/{orderId}/approve")
    public MerchantOrder approve(
            @PathVariable Long orderId,
            @RequestParam Long merchantId) {
        return orderService.approveOrder(orderId, merchantId);
    }

    @PostMapping("/{orderId}/ship")
    public MerchantOrder ship(@PathVariable Long orderId) {
        return orderService.shipOrder(orderId);
    }

    @PostMapping("/{orderId}/deliver")
    public MerchantOrder deliver(@PathVariable Long orderId) {
        return orderService.deliverOrder(orderId);
    }
}

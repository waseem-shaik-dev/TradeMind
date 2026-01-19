package com.trademind.order.controller;

import com.trademind.order.dto.CreateOrderRequest;
import com.trademind.order.dto.OrderResponse;
import com.trademind.order.entity.CustomerOrder;
import com.trademind.order.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final CustomerOrderService orderService;

    @PostMapping
    public OrderResponse placeOrder(@RequestBody CreateOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @GetMapping("/customer/{customerId}")
    public List<CustomerOrder> getOrders(
            @PathVariable Long customerId) {
        return orderService.getOrders(customerId);
    }
}

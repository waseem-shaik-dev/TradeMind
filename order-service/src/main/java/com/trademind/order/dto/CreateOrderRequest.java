package com.trademind.order.dto;

import com.trademind.merchantorder.dto.OrderItemRequest;

import java.util.List;

public record CreateOrderRequest(
        Long customerId,
        Long addressId,
        List<OrderItemRequest> items
) {}

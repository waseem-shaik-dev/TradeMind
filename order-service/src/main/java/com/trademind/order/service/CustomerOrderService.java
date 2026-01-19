package com.trademind.order.service;

import com.trademind.order.dto.CreateOrderRequest;
import com.trademind.order.dto.OrderResponse;
import com.trademind.order.entity.CustomerOrder;

import java.util.List;

public interface CustomerOrderService {

    OrderResponse placeOrder(CreateOrderRequest request);

    List<CustomerOrder> getOrders(Long customerId);
}


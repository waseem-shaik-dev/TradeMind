package com.trademind.order.service.tracking;

import com.trademind.order.entity.Order;
import com.trademind.order.enums.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderTrackingFlowResolver {

    public List<OrderStatus> resolveFlow(Order order) {

        // -----------------------------
        // EDGE CASES FIRST
        // -----------------------------

        if (order.getOrderStatus() == OrderStatus.FAILED) {
            return List.of(OrderStatus.CREATED, OrderStatus.FAILED);
        }

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            return List.of(OrderStatus.CREATED, OrderStatus.CANCELLED);
        }

        if (order.getOrderStatus() == OrderStatus.RETURNED) {
            return List.of(
                    OrderStatus.CREATED,
                    OrderStatus.DELIVERED,
                    OrderStatus.RETURNED
            );
        }

        if (order.getOrderStatus() == OrderStatus.REFUNDED) {
            return List.of(
                    OrderStatus.CREATED,
                    OrderStatus.DELIVERED,
                    OrderStatus.REFUNDED
            );
        }

        // -----------------------------
        // NORMAL FLOW
        // -----------------------------

        List<OrderStatus> flow = new ArrayList<>();

        flow.add(OrderStatus.CREATED);
        flow.add(OrderStatus.AWAITING_ACCEPTANCE);
        flow.add(OrderStatus.ACCEPTED);
        flow.add(OrderStatus.PROCESSING);

        if (order.getDeliveryType() == DeliveryType.SELF_PICKUP) {
            flow.add(OrderStatus.PACKED);
        } else {
            flow.add(OrderStatus.OUT_FOR_DELIVERY);
        }

        flow.add(OrderStatus.DELIVERED);

        return flow;
    }
}
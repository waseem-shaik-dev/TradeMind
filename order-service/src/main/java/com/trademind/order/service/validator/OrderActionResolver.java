package com.trademind.order.service.validator;

import com.trademind.order.entity.Order;
import com.trademind.order.enums.*;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderActionResolver {

    public List<OrderAction> resolveActions(
            Order order,
            OrderActor actor
    ) {

        List<OrderAction> actions = new ArrayList<>();

        OrderStatus status = order.getOrderStatus();

        switch (actor) {

            case CUSTOMER -> {
                if (status == OrderStatus.AWAITING_ACCEPTANCE || status == OrderStatus.CREATED) {
                    actions.add(OrderAction.CANCEL);
                }
            }

            case MERCHANT, RETAILER -> {

                if (status == OrderStatus.AWAITING_ACCEPTANCE || status == OrderStatus.CREATED) {
                    actions.add(OrderAction.ACCEPT);
                    actions.add(OrderAction.REJECT);
                }

                if (status == OrderStatus.ACCEPTED)
                    actions.add(OrderAction.MARK_PROCESSING);

                if (status == OrderStatus.PROCESSING) {
                    if (order.getDeliveryType() == DeliveryType.SELF_PICKUP)
                        actions.add(OrderAction.MARK_PACKED);
                    else
                        actions.add(OrderAction.MARK_OUT_FOR_DELIVERY);
                }

                if (status == OrderStatus.PACKED ||
                        status == OrderStatus.OUT_FOR_DELIVERY)
                    actions.add(OrderAction.MARK_DELIVERED);

                if (order.getPaymentMethod() == PaymentMethod.COD &&
                        order.getPaymentStatus() == PaymentStatus.PENDING)
                    actions.add(OrderAction.MARK_PAYMENT_RECEIVED_COD);
            }


            case ADMIN -> {
                if (status != OrderStatus.DELIVERED &&
                        status != OrderStatus.CANCELLED)
                    actions.add(OrderAction.CANCEL);
            }

            default -> {}
        }

        return actions;
    }
}

package com.trademind.order.service.validator;

import com.trademind.order.entity.Order;
import com.trademind.order.enums.*;

import org.springframework.stereotype.Component;

@Component
public class OrderStateMachineValidator {

    public void validateTransition(
            Order order,
            OrderAction action,
            OrderActor actor
    ) {

        OrderStatus current = order.getOrderStatus();

        switch (action) {

            case ACCEPT -> {


                if (actor != OrderActor.MERCHANT &&
                        actor != OrderActor.RETAILER)
                    throw new IllegalStateException("Only merchant/retailer can accept");

                if (current != OrderStatus.AWAITING_ACCEPTANCE && current!= OrderStatus.CREATED)
                    throw new IllegalStateException("Order neither awaiting acceptance nor created");

            }

            case REJECT -> {
                if (actor != OrderActor.MERCHANT &&
                        actor != OrderActor.RETAILER)
                    throw new IllegalStateException("Only merchant/retailer can reject");

                if (current != OrderStatus.AWAITING_ACCEPTANCE && current!= OrderStatus.CREATED)
                    throw new IllegalStateException("Order neither awaiting acceptance nor created");

            }

            case MARK_PROCESSING -> {
                if (current != OrderStatus.ACCEPTED)
                    throw new IllegalStateException("Must accept first");

            }

            case MARK_PACKED -> {
                if (current != OrderStatus.PROCESSING)
                    throw new IllegalStateException("Order must be PROCESSING");

                if (order.getDeliveryType() != DeliveryType.SELF_PICKUP)
                    throw new IllegalStateException("PACKED allowed only for SELF_PICKUP");
            }

            case MARK_OUT_FOR_DELIVERY -> {
                if (current != OrderStatus.PROCESSING)
                    throw new IllegalStateException("Invalid state");

                if (order.getDeliveryType() == DeliveryType.SELF_PICKUP)
                    throw new IllegalStateException("Use PACKED for self pickup");
            }

            case MARK_DELIVERED -> {
                if (current != OrderStatus.OUT_FOR_DELIVERY &&
                        current != OrderStatus.PACKED)
                    throw new IllegalStateException("Cannot deliver now");
            }

            case CANCEL -> {
                if (current != OrderStatus.AWAITING_ACCEPTANCE)
                    throw new IllegalStateException("Cannot cancel now");

            }

            case MARK_PAYMENT_RECEIVED_COD -> {
                if (order.getPaymentMethod() != PaymentMethod.COD)
                    throw new IllegalStateException("Not a COD order");
            }

            default -> throw new IllegalArgumentException("Unsupported action");
        }
    }
}

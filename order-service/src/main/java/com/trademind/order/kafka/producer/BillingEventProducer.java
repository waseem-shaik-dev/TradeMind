package com.trademind.order.kafka.producer;

import com.trademind.events.order.OrderBillingEvent;
import com.trademind.order.entity.Order;
import com.trademind.order.entity.OrderLineItem;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BillingEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "order.billing.generate";

    public void publishBillingEvent(Order order) {

        OrderBillingEvent event = new OrderBillingEvent(

                order.getId(),
                order.getOrderNumber(),

                order.getUserId(),
                order.getSourceId(),
                order.getSourceType().name(),

                order.getPaymentMethod().name(),
                order.getPaymentStatus().name(),

                order.getSubtotalAmount(),
                order.getTaxAmount(),
                order.getDiscountAmount(),
                order.getDeliveryFee(),
                order.getGrandTotal(),
                order.getCurrency(),

                mapAddress(order),
                mapItems(order.getLineItems())
        );

        kafkaTemplate.send(TOPIC, event);
    }

    private com.trademind.events.order.OrderAddressDto mapAddress(Order order) {

        var a = order.getAddressSnapshot();

        return new com.trademind.events.order.OrderAddressDto(
                a.getFullName(),
                a.getPhone(),
                a.getAddressLine1(),
                a.getAddressLine2(),
                a.getCity(),
                a.getState(),
                a.getPostalCode(),
                a.getCountry()
        );
    }

    private List<com.trademind.events.order.OrderItemDto> mapItems(List<OrderLineItem> items) {

        return items.stream()
                .map(i -> new com.trademind.events.order.OrderItemDto(
                        i.getProductId(),
                        i.getProductName(),
                        i.getSku(),
                        i.getImageUrl(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getTotalPrice()
                ))
                .toList();
    }
}
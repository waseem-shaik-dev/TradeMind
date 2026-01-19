package com.trademind.order.serviceImpl;

import com.trademind.merchantorder.dto.OrderItemRequest;
import com.trademind.merchantorder.enums.OrderStatus;
import com.trademind.order.dto.CreateOrderRequest;
import com.trademind.order.dto.OrderResponse;
import com.trademind.order.entity.CustomerOrder;
import com.trademind.order.entity.CustomerOrderItem;
import com.trademind.order.entity.OrderTracking;
import com.trademind.order.kafka.OrderEventProducer;
import com.trademind.order.repository.CustomerOrderItemRepository;
import com.trademind.order.repository.CustomerOrderRepository;
import com.trademind.order.repository.OrderTrackingRepository;
import com.trademind.order.service.CustomerOrderService;
import com.trademind.order.webclient.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final CustomerOrderRepository orderRepo;
    private final CustomerOrderItemRepository itemRepo;
    private final OrderTrackingRepository trackingRepo;
    private final ProductClient productClient;
    private final OrderEventProducer producer;

    @Override
    public OrderResponse placeOrder(CreateOrderRequest request) {

        CustomerOrder order = orderRepo.save(
                CustomerOrder.builder()
                        .customerId(request.customerId())
                        .status(OrderStatus.CREATED)
                        .orderDate(LocalDateTime.now())
                        .totalAmount(BigDecimal.ZERO)
                        .build()
        );

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest item : request.items()) {

            BigDecimal price = productClient.getPrice(item.productId());
            BigDecimal lineTotal =
                    price.multiply(BigDecimal.valueOf(item.quantity()));

            itemRepo.save(new CustomerOrderItem(
                    null,
                    order.getId(),
                    item.productId(),
                    item.quantity(),
                    price,
                    lineTotal
            ));

            producer.publishStockReserve(
                    item.productId(),
                    item.quantity(),
                    order.getId()
            );

            total = total.add(lineTotal);
        }

        order.setTotalAmount(total);
        orderRepo.save(order);

        trackingRepo.save(new OrderTracking(
                null,
                order.getId(),
                OrderStatus.CREATED,
                LocalDateTime.now()
        ));

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(total)
                .status(OrderStatus.CREATED)
                .build();
    }

    @Override
    public List<CustomerOrder> getOrders(Long customerId) {
        return orderRepo.findByCustomerId(customerId);
    }
}


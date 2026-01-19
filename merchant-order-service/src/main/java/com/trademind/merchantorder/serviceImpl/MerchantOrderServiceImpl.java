package com.trademind.merchantorder.serviceImpl;

import com.trademind.merchantorder.dto.CreateMerchantOrderRequest;
import com.trademind.merchantorder.dto.OrderItemRequest;
import com.trademind.merchantorder.entity.*;
import com.trademind.merchantorder.enums.OrderStatus;
import com.trademind.merchantorder.kafka.MerchantOrderEventProducer;
import com.trademind.merchantorder.repository.*;
import com.trademind.merchantorder.service.MerchantOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MerchantOrderServiceImpl implements MerchantOrderService {

    private final MerchantOrderRepository orderRepo;
    private final MerchantOrderItemRepository itemRepo;
    private final OrderApprovalRepository approvalRepo;
    private final DeliveryScheduleRepository deliveryRepo;
    private final OrderStatusHistoryRepository historyRepo;
    private final MerchantOrderEventProducer producer;

    @Override
    public MerchantOrder createOrder(CreateMerchantOrderRequest request) {

        MerchantOrder order = orderRepo.save(
                MerchantOrder.builder()
                        .retailerId(request.retailerId())
                        .merchantId(request.merchantId())
                        .status(OrderStatus.CREATED)
                        .orderDate(LocalDateTime.now())
                        .build()
        );

        for (OrderItemRequest item : request.items()) {
            itemRepo.save(new MerchantOrderItem(
                    null,
                    order.getId(),
                    item.productId(),
                    item.quantity(),
                    BigDecimal.ZERO
            ));
        }

        saveStatus(order.getId(), OrderStatus.CREATED);
        return order;
    }

    @Override
    public MerchantOrder approveOrder(Long orderId, Long merchantId) {

        MerchantOrder order = getOrder(orderId);

        order.setStatus(OrderStatus.APPROVED);
        orderRepo.save(order);

        approvalRepo.save(new OrderApproval(
                null,
                orderId,
                merchantId,
                LocalDateTime.now(),
                OrderStatus.APPROVED
        ));

        saveStatus(orderId, OrderStatus.APPROVED);
        return order;
    }

    @Override
    public MerchantOrder shipOrder(Long orderId) {

        MerchantOrder order = getOrder(orderId);
        order.setStatus(OrderStatus.SHIPPED);
        orderRepo.save(order);

        deliveryRepo.save(new DeliverySchedule(
                null,
                orderId,
                LocalDate.now().plusDays(3),
                null
        ));

        saveStatus(orderId, OrderStatus.SHIPPED);
        return order;
    }

    @Override
    public MerchantOrder deliverOrder(Long orderId) {

        MerchantOrder order = getOrder(orderId);
        order.setStatus(OrderStatus.DELIVERED);
        orderRepo.save(order);

        deliveryRepo.findById(orderId).ifPresent(d -> {
            d.setActualDeliveryDate(LocalDate.now());
            deliveryRepo.save(d);
        });

        // STOCK IN event
        itemRepo.findByOrderId(orderId).forEach(item ->
                producer.publishStockIn(
                        item.getProductId(),
                        item.getQuantity(),
                        orderId
                )
        );

        saveStatus(orderId, OrderStatus.DELIVERED);
        return order;
    }

    private MerchantOrder getOrder(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private void saveStatus(Long orderId, OrderStatus status) {
        historyRepo.save(new OrderStatusHistory(
                null,
                orderId,
                status,
                LocalDateTime.now()
        ));
    }
}


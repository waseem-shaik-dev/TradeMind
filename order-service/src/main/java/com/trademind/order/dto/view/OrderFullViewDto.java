package com.trademind.order.dto.view;

import com.trademind.order.dto.response.OrderAddressDto;
import com.trademind.order.dto.response.OrderLineItemDto;
import com.trademind.order.dto.response.OrderPricingDto;
import com.trademind.order.enums.*;

import java.time.LocalDateTime;
import java.util.List;

public record OrderFullViewDto(

        Long id,
        String orderNumber,

        Long userId,
        Long sourceId,
        SourceType sourceType,

        DeliveryType deliveryType,

        OrderStatus orderStatus,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,

        OrderPricingDto pricing,

        OrderAddressDto address,

        List<OrderLineItemDto> items,

        List<OrderAction> availableActions,

        LocalDateTime createdAt,
        LocalDateTime updatedAt ,
        Boolean invoiceAvailable

) {}

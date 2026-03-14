package com.trademind.order.dto.request;

import com.trademind.order.enums.OrderAction;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequestDto(

        @NotNull
        OrderAction action,

        String reason

) {}

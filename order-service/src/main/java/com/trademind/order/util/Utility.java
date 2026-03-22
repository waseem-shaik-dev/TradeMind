package com.trademind.order.util;

import com.trademind.order.entity.Order;
import com.trademind.order.enums.OrderStatus;
import com.trademind.order.enums.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class Utility {
    public Boolean eligibleForBilling(Order order){
        return order.getPaymentStatus()== PaymentStatus.PAID ;
    }
}

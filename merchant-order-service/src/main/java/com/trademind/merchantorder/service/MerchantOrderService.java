package com.trademind.merchantorder.service;

import com.trademind.merchantorder.dto.CreateMerchantOrderRequest;
import com.trademind.merchantorder.entity.MerchantOrder;

public interface MerchantOrderService {

    MerchantOrder createOrder(CreateMerchantOrderRequest request);

    MerchantOrder approveOrder(Long orderId, Long merchantId);

    MerchantOrder shipOrder(Long orderId);

    MerchantOrder deliverOrder(Long orderId);
}

package com.trademind.payment.feign;

import com.trademind.payment.feign.dto.CheckoutPaymentViewDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "checkout-service",
        url = "http://localhost:8093" // adjust port
)
public interface CheckoutClient {

    @GetMapping("/api/checkout/{checkoutId}/payment-view")
    CheckoutPaymentViewDto getCheckoutForPayment(
            @PathVariable Long checkoutId
    );
}

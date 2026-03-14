package com.trademind.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "inventory-client",
        url = "http://localhost:8084"
)
public interface InventoryClient {

    @PostMapping("/api/inventories/cancel/{checkoutId}")
    void cancelCommittedStock(
            @PathVariable("checkoutId") Long checkoutId
    );

}

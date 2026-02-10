package com.trademind.cart.kafka;

import com.trademind.cart.service.CartService;
import com.trademind.events.checkout.CheckoutCancelledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartCheckoutEventConsumer {

    private final CartService cartService;

    /**
     * Unlock cart if checkout is cancelled or expired
     */
    @KafkaListener(
            topics = "checkout.cancelled",
            groupId = "cart-service"
    )
    public void handleCheckoutCancelled(
            CheckoutCancelledEvent event
    ) {
        cartService.unlockCartAfterCheckoutFailure(
                event.checkoutId()
        );
    }
}

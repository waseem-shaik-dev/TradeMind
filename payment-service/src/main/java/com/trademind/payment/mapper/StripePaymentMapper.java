package com.trademind.payment.mapper;

import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import com.trademind.payment.entity.PaymentTransaction;
import com.trademind.payment.enums.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentMapper {

    /**
     * Maps Stripe PaymentIntent creation result
     * to PaymentTransaction fields.
     */
    public void mapFromPaymentIntent(
            PaymentTransaction payment,
            PaymentIntent intent
    ) {
        payment.setProviderPaymentIntentId(intent.getId());
        payment.setProviderClientSecret(intent.getClientSecret());
        payment.setStatus(PaymentStatus.INITIATED);
    }

    /**
     * Maps successful Stripe charge result
     */
    public void mapSuccessFromIntent(
            PaymentTransaction payment,
            PaymentIntent intent
    ) {
        payment.setProviderPaymentIntentId(intent.getId());
        payment.setProviderChargeId(intent.getLatestCharge());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setFailureReason(null);
    }


    /**
     * Maps Stripe failure
     */
    public void mapFailure(
            PaymentTransaction payment,
            String reason
    ) {
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason(reason);
    }
}

package com.trademind.payment.enums;

public enum PaymentStatus {

    CREATED,        // Payment record created
    INITIATED,      // Gateway intent created
    SUCCESS,        // Payment successful
    FAILED,         // Payment failed
    CANCELLED,      // Cancelled by system/user
    PENDING
}

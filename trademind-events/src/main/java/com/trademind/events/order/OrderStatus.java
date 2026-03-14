package com.trademind.events.order;


public enum OrderStatus {

    CREATED,
    AWAITING_ACCEPTANCE,
    ACCEPTED,
    PROCESSING,
    PACKED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    REJECTED,
    CANCELLED,
    FAILED
}


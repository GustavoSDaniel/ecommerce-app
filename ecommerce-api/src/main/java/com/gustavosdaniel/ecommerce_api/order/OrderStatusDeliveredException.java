package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class OrderStatusDeliveredException extends BaseExceptionRunTime {

    public OrderStatusDeliveredException() {
    }

    public OrderStatusDeliveredException(String message) {
        super(message);
    }

    public OrderStatusDeliveredException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderStatusDeliveredException(Throwable cause) {
        super(cause);
    }

    public OrderStatusDeliveredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

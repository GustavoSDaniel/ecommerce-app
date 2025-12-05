package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class OrderStatusShippedException extends BaseExceptionRunTime {

    public OrderStatusShippedException() {
    }

    public OrderStatusShippedException(String message) {
        super(message);
    }

    public OrderStatusShippedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderStatusShippedException(Throwable cause) {
        super(cause);
    }

    public OrderStatusShippedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

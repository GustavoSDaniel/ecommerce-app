package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class OrderStatusCanceledException extends BaseExceptionRunTime {

    public OrderStatusCanceledException() {
    }

    public OrderStatusCanceledException(String message) {
        super(message);
    }

    public OrderStatusCanceledException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderStatusCanceledException(Throwable cause) {
        super(cause);
    }

    public OrderStatusCanceledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class OrderStatusPaidException extends BaseExceptionRunTime {

    public OrderStatusPaidException() {
    }

    public OrderStatusPaidException(String message) {
        super(message);
    }

    public OrderStatusPaidException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderStatusPaidException(Throwable cause) {
        super(cause);
    }

    public OrderStatusPaidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

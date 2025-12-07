package com.gustavosdaniel.ecommerce_api.order;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class OrderNotFoundException extends BaseExceptionRunTime {

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Throwable cause) {
        super(cause);
    }

    public OrderNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

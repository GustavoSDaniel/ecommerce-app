package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class ProductNotFoundException extends BaseExceptionRunTime {

    public ProductNotFoundException() {
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProductNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

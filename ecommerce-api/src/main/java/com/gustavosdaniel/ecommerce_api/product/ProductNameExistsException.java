package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class ProductNameExistsException extends BaseExceptionRunTime {

    public ProductNameExistsException() {
    }

    public ProductNameExistsException(String message) {
        super(message);
    }

    public ProductNameExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNameExistsException(Throwable cause) {
        super(cause);
    }

    public ProductNameExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

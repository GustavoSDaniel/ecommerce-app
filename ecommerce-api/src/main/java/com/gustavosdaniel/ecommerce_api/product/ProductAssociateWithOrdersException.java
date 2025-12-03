package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class ProductAssociateWithOrdersException extends BaseExceptionRunTime {

    public ProductAssociateWithOrdersException() {
    }

    public ProductAssociateWithOrdersException(String message) {
        super(message);
    }

    public ProductAssociateWithOrdersException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductAssociateWithOrdersException(Throwable cause) {
        super(cause);
    }

    public ProductAssociateWithOrdersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.gustavosdaniel.ecommerce_api.exception;

public class BaseExceptionBusiness extends Exception {

    public BaseExceptionBusiness() {
    }

    public BaseExceptionBusiness(String message) {
        super(message);
    }

    public BaseExceptionBusiness(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseExceptionBusiness(Throwable cause) {
        super(cause);
    }

    public BaseExceptionBusiness(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

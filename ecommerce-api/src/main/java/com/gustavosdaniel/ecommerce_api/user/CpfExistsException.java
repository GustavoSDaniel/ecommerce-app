package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class CpfExistsException extends BaseExceptionRunTime {

    public CpfExistsException() {
    }

    public CpfExistsException(String message) {
        super(message);
    }

    public CpfExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CpfExistsException(Throwable cause) {
        super(cause);
    }

    public CpfExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

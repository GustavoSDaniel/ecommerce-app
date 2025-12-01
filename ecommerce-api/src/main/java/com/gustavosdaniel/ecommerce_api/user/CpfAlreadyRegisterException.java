package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class CpfAlreadyRegisterException extends BaseExceptionRunTime {

    public CpfAlreadyRegisterException() {
    }

    public CpfAlreadyRegisterException(String message) {
        super(message);
    }

    public CpfAlreadyRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CpfAlreadyRegisterException(Throwable cause) {
        super(cause);
    }

    public CpfAlreadyRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

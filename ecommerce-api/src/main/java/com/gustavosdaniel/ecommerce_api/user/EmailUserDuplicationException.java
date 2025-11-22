package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class EmailUserDuplicationException extends BaseExceptionRunTime {

    public EmailUserDuplicationException() {
    }

    public EmailUserDuplicationException(String message) {
        super(message);
    }

    public EmailUserDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailUserDuplicationException(Throwable cause) {
        super(cause);
    }

    public EmailUserDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

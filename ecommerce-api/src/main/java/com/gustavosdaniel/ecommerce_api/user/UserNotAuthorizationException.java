package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class UserNotAuthorizationException extends BaseExceptionRunTime {
    public UserNotAuthorizationException() {
    }

    public UserNotAuthorizationException(String message) {
        super(message);
    }

    public UserNotAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotAuthorizationException(Throwable cause) {
        super(cause);
    }

    public UserNotAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

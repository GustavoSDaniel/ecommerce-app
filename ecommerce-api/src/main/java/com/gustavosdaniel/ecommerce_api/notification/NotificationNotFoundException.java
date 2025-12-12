package com.gustavosdaniel.ecommerce_api.notification;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class NotificationNotFoundException extends BaseExceptionRunTime {
    public NotificationNotFoundException() {
    }

    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationNotFoundException(Throwable cause) {
        super(cause);
    }

    public NotificationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

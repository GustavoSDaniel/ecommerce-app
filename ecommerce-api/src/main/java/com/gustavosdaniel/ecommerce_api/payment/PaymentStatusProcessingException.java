package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class PaymentStatusProcessingException extends BaseExceptionRunTime {

    public PaymentStatusProcessingException() {
    }

    public PaymentStatusProcessingException(String message) {
        super(message);
    }

    public PaymentStatusProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentStatusProcessingException(Throwable cause) {
        super(cause);
    }

    public PaymentStatusProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

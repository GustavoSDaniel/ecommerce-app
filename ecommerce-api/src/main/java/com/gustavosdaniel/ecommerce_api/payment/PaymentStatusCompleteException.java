package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class PaymentStatusCompleteException extends BaseExceptionRunTime {

    public PaymentStatusCompleteException() {
    }

    public PaymentStatusCompleteException(String message) {
        super(message);
    }

    public PaymentStatusCompleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentStatusCompleteException(Throwable cause) {
        super(cause);
    }

    public PaymentStatusCompleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

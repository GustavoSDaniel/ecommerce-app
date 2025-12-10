package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class PaymentStatusCancelledException extends BaseExceptionRunTime {

    public PaymentStatusCancelledException() {
    }

    public PaymentStatusCancelledException(String message) {
        super(message);
    }

    public PaymentStatusCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentStatusCancelledException(Throwable cause) {
        super(cause);
    }

    public PaymentStatusCancelledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

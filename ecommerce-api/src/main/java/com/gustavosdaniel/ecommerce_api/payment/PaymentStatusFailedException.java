package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class PaymentStatusFailedException extends BaseExceptionRunTime {

    public PaymentStatusFailedException() {
    }

    public PaymentStatusFailedException(String message) {
        super(message);
    }

    public PaymentStatusFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentStatusFailedException(Throwable cause) {
        super(cause);
    }

    public PaymentStatusFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

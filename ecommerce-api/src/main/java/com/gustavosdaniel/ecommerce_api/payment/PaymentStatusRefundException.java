package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class PaymentStatusRefundException extends BaseExceptionRunTime {

    public PaymentStatusRefundException() {
    }

    public PaymentStatusRefundException(String message) {
        super(message);
    }

    public PaymentStatusRefundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentStatusRefundException(Throwable cause) {
        super(cause);
    }

    public PaymentStatusRefundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

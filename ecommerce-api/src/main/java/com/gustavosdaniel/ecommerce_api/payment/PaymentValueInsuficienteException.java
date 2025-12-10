package com.gustavosdaniel.ecommerce_api.payment;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class PaymentValueInsuficienteException extends BaseExceptionRunTime {
    public PaymentValueInsuficienteException() {
    }

    public PaymentValueInsuficienteException(String message) {
        super(message);
    }

    public PaymentValueInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentValueInsuficienteException(Throwable cause) {
        super(cause);
    }

    public PaymentValueInsuficienteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

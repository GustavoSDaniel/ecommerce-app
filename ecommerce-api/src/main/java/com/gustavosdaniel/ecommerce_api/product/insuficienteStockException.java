package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionBusiness;

public class insuficienteStockException extends BaseExceptionBusiness {
    public insuficienteStockException() {
    }

    public insuficienteStockException(String message) {
        super(message);
    }

    public insuficienteStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public insuficienteStockException(Throwable cause) {
        super(cause);
    }

    public insuficienteStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

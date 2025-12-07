package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionBusiness;

public class InsuficienteStockException extends BaseExceptionBusiness {
    public InsuficienteStockException() {
    }

    public InsuficienteStockException(String message) {
        super(message);
    }

    public InsuficienteStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsuficienteStockException(Throwable cause) {
        super(cause);
    }

    public InsuficienteStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

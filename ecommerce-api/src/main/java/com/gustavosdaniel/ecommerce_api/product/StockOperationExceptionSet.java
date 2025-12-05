package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionBusiness;

public class StockOperationExceptionSet extends BaseExceptionBusiness {
    public StockOperationExceptionSet() {
    }

    public StockOperationExceptionSet(String message) {
        super(message);
    }

    public StockOperationExceptionSet(String message, Throwable cause) {
        super(message, cause);
    }

    public StockOperationExceptionSet(Throwable cause) {
        super(cause);
    }

    public StockOperationExceptionSet(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

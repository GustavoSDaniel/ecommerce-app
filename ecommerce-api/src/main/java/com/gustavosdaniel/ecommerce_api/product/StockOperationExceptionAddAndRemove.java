package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionBusiness;

public class StockOperationExceptionAddAndRemove extends BaseExceptionBusiness {
    public StockOperationExceptionAddAndRemove() {
    }

    public StockOperationExceptionAddAndRemove(String message) {
        super(message);
    }

    public StockOperationExceptionAddAndRemove(String message, Throwable cause) {
        super(message, cause);
    }

    public StockOperationExceptionAddAndRemove(Throwable cause) {
        super(cause);
    }

    public StockOperationExceptionAddAndRemove(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

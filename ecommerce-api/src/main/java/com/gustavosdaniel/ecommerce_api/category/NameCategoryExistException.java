package com.gustavosdaniel.ecommerce_api.category;

import com.gustavosdaniel.ecommerce_api.exception.BaseExceptionRunTime;

public class NameCategoryExistException extends BaseExceptionRunTime {
    public NameCategoryExistException() {
    }

    public NameCategoryExistException(String message) {
        super(message);
    }

    public NameCategoryExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameCategoryExistException(Throwable cause) {
        super(cause);
    }

    public NameCategoryExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

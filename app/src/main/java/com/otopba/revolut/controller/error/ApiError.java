package com.otopba.revolut.controller.error;

import androidx.annotation.Nullable;

public class ApiError extends CurrencyError {

    public ApiError() {
    }

    public ApiError(@Nullable Throwable throwable) {
        super(throwable);
    }

}

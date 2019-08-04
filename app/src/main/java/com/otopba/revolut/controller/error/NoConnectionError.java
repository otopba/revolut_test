package com.otopba.revolut.controller.error;

import androidx.annotation.Nullable;

public class NoConnectionError extends CurrencyError {

    public NoConnectionError() {
    }

    public NoConnectionError(@Nullable Throwable throwable) {
        super(throwable);
    }

}

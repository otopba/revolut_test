package com.otopba.revolut.controller.error;

import androidx.annotation.Nullable;

public abstract class CurrencyError {

    public final Throwable throwable;

    public CurrencyError() {
        throwable = null;
    }

    public CurrencyError(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }
}

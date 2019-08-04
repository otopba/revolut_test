package com.otopba.revolut.ui.currency;

import androidx.annotation.NonNull;

public class CurrencyValue {

    public final CurrencyModel model;
    public final String value;

    public CurrencyValue(@NonNull CurrencyModel model, @NonNull String value) {
        this.model = model;
        this.value = value;
    }

}

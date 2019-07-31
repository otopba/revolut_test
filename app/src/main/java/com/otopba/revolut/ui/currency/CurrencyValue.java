package com.otopba.revolut.ui.currency;

import androidx.annotation.NonNull;

public class CurrencyValue {

    private CurrencyModel model;
    private String value;

    public CurrencyValue(@NonNull CurrencyModel model, @NonNull String value) {
        this.model = model;
        this.value = value;
    }

    @NonNull
    public CurrencyModel getModel() {
        return model;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }
}

package com.otopba.revolut.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otopba.revolut.Currency;

import java.util.Map;

public interface CurrencyController {

    void setMainCurrency(@NonNull Currency currency);

    void setMainCurrencyValue(double value);

    void registerListener(@NonNull Listener listener);

    void unregisterListener(@NonNull Listener listener);

    interface Listener {
        void onUpdate(@NonNull Map<Currency, Double> values, @Nullable Currency mainCurrency);
    }
}

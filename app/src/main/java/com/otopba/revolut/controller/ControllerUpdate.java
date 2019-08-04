package com.otopba.revolut.controller;

import androidx.annotation.NonNull;

import com.otopba.revolut.storage.Currency;

import java.util.Map;

public class ControllerUpdate {

    public final Map<Currency, Float> values;
    public final Currency mainCurrency;
    public final long date;


    public ControllerUpdate(@NonNull Map<Currency, Float> values, @NonNull Currency mainCurrency, long date) {
        this.values = values;
        this.mainCurrency = mainCurrency;
        this.date = date;
    }

    @Override
    public String toString() {
        return "ControllerUpdate{" +
                "values=" + values +
                ", mainCurrency=" + mainCurrency +
                ", date=" + date +
                '}';
    }
}

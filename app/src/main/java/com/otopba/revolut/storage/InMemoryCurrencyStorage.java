package com.otopba.revolut.storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otopba.revolut.Currency;

import java.util.Collections;
import java.util.Map;

public class InMemoryCurrencyStorage implements CurrencyStorage {

    private String date;
    private Map<Currency, Float> rates;

    @Override
    public void saveRates(@NonNull Map<Currency, Float> rates, @NonNull String date) {
        this.rates = rates;
        this.date = date;
    }

    @NonNull
    @Override
    public Map<Currency, Float> getRates() {
        if (rates == null) {
            return Collections.emptyMap();
        }
        return rates;
    }

    @Override
    public float getRate(@NonNull Currency currency) {
        if (rates == null) {
            return 0;
        }
        Float value = rates.get(currency);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Nullable
    @Override
    public String getDate() {
        return date;
    }

}

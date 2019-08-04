package com.otopba.revolut.storage;

import androidx.annotation.NonNull;

import com.otopba.revolut.Currency;

import java.util.Collections;
import java.util.Map;

public class InMemoryCurrencyStorage implements CurrencyStorage {

    private long date;
    private Map<Currency, Float> rates;

    @Override
    public void saveRates(@NonNull Map<Currency, Float> rates, long date) {
        this.rates = rates;
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

}

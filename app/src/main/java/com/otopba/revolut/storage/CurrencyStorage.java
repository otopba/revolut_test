package com.otopba.revolut.storage;

import androidx.annotation.NonNull;

import com.otopba.revolut.Currency;

import java.util.Map;

public interface CurrencyStorage {

    void saveRates(@NonNull Map<Currency, Double> rates, long date);

    @NonNull
    Map<Currency, Double> getRates();

    double getRate(@NonNull Currency currency);

}

package com.otopba.revolut.storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otopba.revolut.Currency;

import java.util.Map;

public interface CurrencyStorage {

    void saveRates(@NonNull Map<Currency, Float> rates, String date);

    @NonNull
    Map<Currency, Float> getRates();

    float getRate(@NonNull Currency currency);

    @Nullable
    String getDate();

}

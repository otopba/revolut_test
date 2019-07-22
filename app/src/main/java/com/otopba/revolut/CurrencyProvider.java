package com.otopba.revolut;

import androidx.annotation.NonNull;

import io.reactivex.Single;

public interface CurrencyProvider {

    @NonNull
    Single<CurrencyValue> getCurrency();

}

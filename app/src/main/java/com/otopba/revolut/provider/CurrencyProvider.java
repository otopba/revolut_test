package com.otopba.revolut.provider;

import androidx.annotation.NonNull;

import com.otopba.revolut.CurrencyValue;

import io.reactivex.Single;

public interface CurrencyProvider {

    @NonNull
    Single<CurrencyValue> getCurrency();

}

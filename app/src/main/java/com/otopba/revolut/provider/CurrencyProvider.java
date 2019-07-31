package com.otopba.revolut.provider;

import androidx.annotation.NonNull;

import com.otopba.revolut.api.CurrencyUpdate;

import io.reactivex.Single;

public interface CurrencyProvider {

    @NonNull
    Single<CurrencyUpdate> getCurrency();

}

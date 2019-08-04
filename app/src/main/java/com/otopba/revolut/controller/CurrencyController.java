package com.otopba.revolut.controller;

import androidx.annotation.NonNull;

import com.otopba.revolut.controller.error.CurrencyError;
import com.otopba.revolut.storage.Currency;

import io.reactivex.subjects.Subject;

public interface CurrencyController {

    void setMainCurrency(@NonNull Currency currency);

    void setMainCurrencyValue(float value);

    void start();

    void stop();

    @NonNull
    Subject<ControllerUpdate> getUpdateSubject();

    @NonNull
    Subject<CurrencyError> getErrorSubject();

}

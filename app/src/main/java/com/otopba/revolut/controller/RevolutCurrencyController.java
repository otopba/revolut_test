package com.otopba.revolut.controller;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.otopba.revolut.Currency;
import com.otopba.revolut.api.CurrencyUpdate;
import com.otopba.revolut.provider.CurrencyProvider;
import com.otopba.revolut.storage.CurrencyStorage;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RevolutCurrencyController implements CurrencyController {

    private static final long INTERVAL_SEC = 1;
    private final CurrencyStorage storage;
    private final CurrencyProvider provider;
    private Subject<ControllerUpdate> updateSubject;
    private Subject<Throwable> errorSubject;
    private Disposable disposable;
    private Currency mainCurrency;
    private float mainCurrencyValue;

    public RevolutCurrencyController(@NonNull CurrencyStorage storage, @NonNull CurrencyProvider provider) {
        this.storage = storage;
        this.provider = provider;
    }

    @Override
    public void start() {
        if (disposable == null || disposable.isDisposed()) {
            disposable = Observable.interval(0, INTERVAL_SEC, TimeUnit.SECONDS)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .flatMap((Function<Long, ObservableSource<CurrencyUpdate>>) aLong -> provider.getCurrency().toObservable())
                    .subscribe(this::updateCurrency, throwable -> notifyError(throwable));
        }
    }

    @Override
    public void stop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    @NonNull
    @Override
    public Subject<ControllerUpdate> getUpdateSubject() {
        if (updateSubject == null) {
            updateSubject = BehaviorSubject.create();
        }
        return updateSubject;
    }

    @NonNull
    @Override
    public Subject<Throwable> getErrorSubject() {
        if (errorSubject == null) {
            errorSubject = PublishSubject.create();
        }
        return errorSubject;
    }

    private void updateCurrency(@NonNull CurrencyUpdate currencyUpdate) {
        storage.saveRates(currencyUpdate.rates, Calendar.getInstance().getTime().getTime());
        sync();
    }

    @Override
    public void setMainCurrency(@NonNull Currency currency) {
        if (mainCurrency == currency) {
            return;
        }
        if (mainCurrency == null) {
            mainCurrencyValue = storage.getRate(currency);
        } else {
            float mainRate = storage.getRate(mainCurrency);
            float factor = mainRate == 0 ? 0 : mainCurrencyValue / mainRate;
            mainCurrencyValue = storage.getRate(currency) * factor;
        }
        mainCurrency = currency;
        sync();
    }

    @Override
    public void setMainCurrencyValue(float value) {
        mainCurrencyValue = value;
        sync();
    }

    private void sync() {
        Map<Currency, Float> rates = storage.getRates();
        if (mainCurrency == null) {
            notifyUpdate(rates);
            return;
        }
        float mainRate = storage.getRate(mainCurrency);
        float factor = mainRate == 0 ? 0 : mainCurrencyValue / mainRate;
        Map<Currency, Float> values = new ArrayMap<>(rates.size());
        for (Map.Entry<Currency, Float> entry : rates.entrySet()) {
            values.put(entry.getKey(), entry.getValue() * factor);
        }
        notifyUpdate(values);
    }

    private void notifyError(Throwable throwable) {
        errorSubject.onNext(throwable);
    }

    private void notifyUpdate(@NonNull Map<Currency, Float> values) {
        updateSubject.onNext(new ControllerUpdate(values, mainCurrency, storage.getDate()));
    }

}

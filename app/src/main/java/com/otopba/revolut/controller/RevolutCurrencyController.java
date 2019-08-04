package com.otopba.revolut.controller;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.otopba.revolut.Currency;
import com.otopba.revolut.api.CurrencyUpdate;
import com.otopba.revolut.provider.CurrencyProvider;
import com.otopba.revolut.storage.CurrencyStorage;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RevolutCurrencyController implements CurrencyController {

    private final Set<Listener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final CurrencyStorage storage;
    private final CurrencyProvider provider;
    private Disposable disposable;
    private Currency mainCurrency;
    private float mainCurrencyValue;

    public RevolutCurrencyController(@NonNull CurrencyStorage storage, @NonNull CurrencyProvider provider) {
        this.storage = storage;
        this.provider = provider;
    }

    private void requestCurrency() {
        if (listeners.isEmpty()) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                disposable = null;
            }
        } else if (disposable == null || disposable.isDisposed()) {
            disposable = provider.getCurrency()
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::updateCurrency);//TODO: обработать ошибки
        }
    }

    private void updateCurrency(@NonNull CurrencyUpdate currencyUpdate) {
        storage.saveRates(currencyUpdate.rates, currencyUpdate.date);
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

    @Override
    public void registerListener(@NonNull Listener listener) {
        listeners.add(listener);
        requestCurrency();
    }

    @Override
    public void unregisterListener(@NonNull Listener listener) {
        listeners.remove(listener);
        requestCurrency();
    }

    private void sync() {
        Map<Currency, Float> rates = storage.getRates();
        if (mainCurrency == null) {
            notifyUpdate(rates, storage.getDate());
            return;
        }
        float mainRate = storage.getRate(mainCurrency);
        float factor = mainRate == 0 ? 0 : mainCurrencyValue / mainRate;
        Map<Currency, Float> values = new ArrayMap<>(rates.size());
        for (Map.Entry<Currency, Float> entry : rates.entrySet()) {
            values.put(entry.getKey(), entry.getValue() * factor);
        }
        notifyUpdate(values, storage.getDate());
    }

    public void notifyUpdate(@NonNull Map<Currency, Float> values, @Nullable String date) {
        notifyListeners(listener -> listener.onUpdate(values, mainCurrency, date));
    }

    public void notifyListeners(@NonNull Consumer<Listener> action) {
        for (Listener listener : listeners) {
            action.accept(listener);
        }
    }

}

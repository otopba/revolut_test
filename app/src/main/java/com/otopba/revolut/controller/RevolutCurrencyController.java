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
    private double mainCurrencyValue;

    public RevolutCurrencyController(@NonNull CurrencyStorage storage, @NonNull CurrencyProvider provider) {
        this.storage = storage;
        this.provider = provider;
        requestCurrency();
    }

    private void requestCurrency() {
        disposable = provider.getCurrency()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(this::updateCurrency);//TODO: обработать ошибки
    }

    private void updateCurrency(@NonNull CurrencyUpdate currencyUpdate) {
        storage.saveRates(currencyUpdate.rates, 0); //TODO: currencyUpdate.date
        sync();
    }

    @Override
    public void setMainCurrency(@NonNull Currency currency) {
        mainCurrency = currency;
        sync();
    }

    @Override
    public void setMainCurrencyValue(double value) {
        mainCurrencyValue = value;
        sync();
    }

    @Override
    public void registerListener(@NonNull Listener listener) {
        listeners.add(listener);
        sync();
    }

    @Override
    public void unregisterListener(@NonNull Listener listener) {
        listeners.remove(listener);
    }

    private void sync() {
        Map<Currency, Double> rates = storage.getRates();
        if (mainCurrency == null) {
            notifyUpdate(rates, null);
            return;
        }
        double mainRate = storage.getRate(mainCurrency);
        double factor = mainCurrencyValue == 0 ? 0 : mainRate / mainCurrencyValue;
        Map<Currency, Double> values = new ArrayMap<>(rates.size());
        for (Map.Entry<Currency, Double> entry : rates.entrySet()) {
            values.put(entry.getKey(), entry.getValue() * factor);
        }
        notifyUpdate(values, mainCurrency);
    }

    public void notifyUpdate(@NonNull Map<Currency, Double> values, @Nullable Currency mainCurrency) {
        notifyListeners(listener -> listener.onUpdate(values, mainCurrency));
    }

    public void notifyListeners(@NonNull Consumer<Listener> action) {
        for (Listener listener : listeners) {
            action.accept(listener);
        }
    }

}

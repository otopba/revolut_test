package com.otopba.revolut.controller;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otopba.revolut.api.CurrencyUpdate;
import com.otopba.revolut.connection.ConnectionManager;
import com.otopba.revolut.controller.error.ApiError;
import com.otopba.revolut.controller.error.CurrencyError;
import com.otopba.revolut.controller.error.NoConnectionError;
import com.otopba.revolut.provider.CurrencyProvider;
import com.otopba.revolut.storage.Currency;
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

    private static final String TAG = RevolutCurrencyController.class.getName();

    private static final long UPDATE_INTERVAL_SEC = 1;
    private final Subject<ControllerUpdate> updateSubject = BehaviorSubject.create();
    private final Subject<CurrencyError> errorSubject = PublishSubject.create();
    private final CurrencyStorage storage;
    private final CurrencyProvider provider;
    private final ConnectionManager connectionManager;
    private Disposable currencyDisposable;
    private Disposable connectionDisposable;

    private volatile Currency mainCurrency;
    private volatile float mainCurrencyValue;

    public RevolutCurrencyController(@NonNull CurrencyStorage storage, @NonNull CurrencyProvider provider, @NonNull ConnectionManager connectionManager) {
        this.storage = storage;
        this.provider = provider;
        this.connectionManager = connectionManager;
    }

    @Override
    public void start() {
        if (connectionDisposable != null && !connectionDisposable.isDisposed()) {
            return;
        }
        connectionDisposable = connectionManager.getConnectionSubject()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onConnection, this::notifyNoConnectionError);
    }

    private void onConnection(boolean connected) {
        Log.d(TAG, String.format("Connection change to %s", connected));
        if (connected) {
            requestCurrency();
        } else {
            disposeCurrency();
            notifyNoConnectionError(null);
        }
    }

    private void requestCurrency() {
        if (currencyDisposable != null && !currencyDisposable.isDisposed()) {
            return;
        }
        currencyDisposable = Observable.interval(0, UPDATE_INTERVAL_SEC, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Long, ObservableSource<CurrencyUpdate>>) aLong -> provider.getCurrency().toObservable())
                .subscribe(this::updateCurrency, this::notifyApiError);
    }

    @Override
    public void stop() {
        disposeCurrency();
        disposeConnection();
    }

    private void disposeConnection() {
        if (connectionDisposable != null && !connectionDisposable.isDisposed()) {
            connectionDisposable.dispose();
            connectionDisposable = null;
        }
    }

    private void disposeCurrency() {
        if (currencyDisposable != null && !currencyDisposable.isDisposed()) {
            currencyDisposable.dispose();
            currencyDisposable = null;
        }
    }

    @NonNull
    @Override
    public Subject<ControllerUpdate> getUpdateSubject() {
        return updateSubject;
    }

    @NonNull
    @Override
    public Subject<CurrencyError> getErrorSubject() {
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

    private void notifyNoConnectionError(@Nullable Throwable throwable) {
        errorSubject.onNext(new NoConnectionError(throwable));
    }

    private void notifyApiError(@Nullable Throwable throwable) {
        errorSubject.onNext(new ApiError(throwable));
    }

    private void notifyUpdate(@NonNull Map<Currency, Float> values) {
        updateSubject.onNext(new ControllerUpdate(values, mainCurrency, storage.getDate()));
    }

}

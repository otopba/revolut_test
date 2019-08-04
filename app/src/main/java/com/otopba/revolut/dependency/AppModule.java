package com.otopba.revolut.dependency;

import android.content.Context;

import androidx.annotation.NonNull;

import com.otopba.revolut.connection.ConnectionManager;
import com.otopba.revolut.connection.ConnectionManagerImpl;
import com.otopba.revolut.controller.CurrencyController;
import com.otopba.revolut.controller.RevolutCurrencyController;
import com.otopba.revolut.provider.CurrencyProvider;
import com.otopba.revolut.provider.RevolutCurrencyProvider;
import com.otopba.revolut.storage.CurrencyStorage;
import com.otopba.revolut.storage.InMemoryCurrencyStorage;
import com.otopba.revolut.utils.Formatter;
import com.otopba.revolut.utils.RevolutFormatter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context context;

    public AppModule(@NonNull Context context) {
        this.context = context;
    }

    @Provides
    public Context context() {
        return context;
    }

    @Provides
    @Singleton
    CurrencyProvider provideApi() {
        return new RevolutCurrencyProvider();
    }

    @Provides
    @Singleton
    CurrencyStorage provideCurrencyStorage() {
        return new InMemoryCurrencyStorage();
    }

    @Provides
    @Singleton
    CurrencyController provideCurrencyController(CurrencyStorage currencyStorage, CurrencyProvider currencyProvider,
                                                 ConnectionManager connectionManager) {
        return new RevolutCurrencyController(currencyStorage, currencyProvider, connectionManager);
    }

    @Provides
    @Singleton
    Formatter provideFormatter() {
        return new RevolutFormatter();
    }

    @Provides
    @Singleton
    ConnectionManager provideConnectionManager(Context context) {
        return new ConnectionManagerImpl(context);
    }

}

package com.otopba.revolut.dependency;

import com.otopba.revolut.controller.CurrencyController;
import com.otopba.revolut.controller.RevolutCurrencyController;
import com.otopba.revolut.provider.CurrencyProvider;
import com.otopba.revolut.provider.RevolutCurrencyProvider;
import com.otopba.revolut.storage.CurrencyStorage;
import com.otopba.revolut.storage.InMemoryCurrencyStorage;
import com.otopba.revolut.utils.Formater;
import com.otopba.revolut.utils.RevolutFormater;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

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
    CurrencyController provideCurrencyController(CurrencyStorage currencyStorage, CurrencyProvider currencyProvider) {
        return new RevolutCurrencyController(currencyStorage, currencyProvider);
    }

    @Provides
    @Singleton
    Formater provideFormater() {
        return new RevolutFormater();
    }

}

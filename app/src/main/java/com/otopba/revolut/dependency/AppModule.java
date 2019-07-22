package com.otopba.revolut.dependency;

import com.otopba.revolut.provider.CurrencyProvider;
import com.otopba.revolut.provider.RevolutProvider;
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
        return new RevolutProvider();
    }

    @Provides
    @Singleton
    Formater provideFormater() {
        return new RevolutFormater();
    }

}

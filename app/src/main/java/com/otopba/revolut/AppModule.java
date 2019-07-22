package com.otopba.revolut;

import com.otopba.revolut.api.RevolutProvider;

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

}

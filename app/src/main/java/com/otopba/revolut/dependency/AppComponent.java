package com.otopba.revolut.dependency;

import com.otopba.revolut.fragment.CurrencyFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(CurrencyFragment fragment);
}

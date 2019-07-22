package com.otopba.revolut.provider;

import androidx.annotation.NonNull;

import com.otopba.revolut.CurrencyValue;
import com.otopba.revolut.api.RevolutApi;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RevolutProvider implements CurrencyProvider {

    private RevolutApi api;

    public RevolutProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(RevolutApi.class);
    }

    @NonNull
    @Override
    public Single<CurrencyValue> getCurrency() {
        return api.getCurrency();
    }

}

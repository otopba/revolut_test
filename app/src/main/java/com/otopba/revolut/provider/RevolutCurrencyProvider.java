package com.otopba.revolut.provider;

import androidx.annotation.NonNull;

import com.otopba.revolut.api.CurrencyUpdate;
import com.otopba.revolut.api.RevolutApi;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RevolutCurrencyProvider implements CurrencyProvider {

    private static final int MAX_FAIL_COUNT = 3;
    private static final int RETRY_DELAY_MS = 1000;
    private RevolutApi api;

    public RevolutCurrencyProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(RevolutApi.class);
    }

    @NonNull
    @Override
    public Single<CurrencyUpdate> getCurrency() {
        return api.getCurrency()
                .retryWhen((Flowable<Throwable> f) -> f.take(MAX_FAIL_COUNT).delay(RETRY_DELAY_MS, TimeUnit.MILLISECONDS));
    }

}

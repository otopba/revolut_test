package com.otopba.revolut.api;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RevolutApi {

    @GET("latest?base=EUR")
    Single<CurrencyUpdate> getCurrency();

}

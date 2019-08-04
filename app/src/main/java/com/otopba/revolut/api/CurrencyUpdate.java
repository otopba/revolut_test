package com.otopba.revolut.api;

import com.otopba.revolut.Currency;

import java.util.Map;

public class CurrencyUpdate {

    public final Currency base;
    public final String date;
    public final Map<Currency, Float> rates;

    public CurrencyUpdate(Currency base, String date, Map<Currency, Float> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "CurrencyUpdate{" +
                "base=" + base +
                ", date='" + date + '\'' +
                ", rates=" + rates +
                '}';
    }

}

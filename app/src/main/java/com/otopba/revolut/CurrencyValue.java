package com.otopba.revolut;

import java.util.Map;

public class CurrencyValue {

    public final Currency base;
    public final String date;
    public final Map<Currency, Double> rates;

    public CurrencyValue(Currency base, String date, Map<Currency, Double> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "CurrencyValue{" +
                "base=" + base +
                ", date='" + date + '\'' +
                ", rates=" + rates +
                '}';
    }

}

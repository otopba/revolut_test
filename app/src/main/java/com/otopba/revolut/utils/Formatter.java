package com.otopba.revolut.utils;

import androidx.annotation.NonNull;

public interface Formatter {

    @NonNull
    String formatToCurrencyValue(float value);

    float formatFromCurrencyValue(@NonNull String text);

    @NonNull
    String formatDate(long date);

}

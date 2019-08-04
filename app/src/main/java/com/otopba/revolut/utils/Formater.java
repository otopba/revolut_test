package com.otopba.revolut.utils;

import androidx.annotation.NonNull;

public interface Formater {

    @NonNull
    String formatToCurrencyValue(float value);

    float formatFromCurrencyValue(@NonNull String text);

}

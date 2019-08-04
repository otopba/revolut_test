package com.otopba.revolut.utils;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RevolutFormatter implements Formatter {

    private static final String DATE_PATTERN = "hh:mm:ss";
    private final NumberFormat numberFormat;
    private final DateFormat dateFormat;

    public RevolutFormatter() {
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
    }

    @NonNull
    public String formatToCurrencyValue(float value) {
        return numberFormat.format(value);
    }

    @Override
    public float formatFromCurrencyValue(@NonNull String text) {
        text = text.trim().replace(",", ".");
        float value;
        try {
            value = Float.parseFloat(text);
        } catch (NumberFormatException ex) {
            value = 0;
        }
        return value;
    }

    @NonNull
    public String formatDate(long date) {
        return dateFormat.format(date);
    }

}

package com.otopba.revolut.utils;

import androidx.annotation.NonNull;

import java.text.NumberFormat;

public class RevolutFormater implements Formater {

    private final NumberFormat formatter;

    public RevolutFormater() {
        formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        formatter.setGroupingUsed(false);
    }

    @NonNull
    public String formatToCurrencyValue(float value) {
        return formatter.format(value);
    }

    @Override
    public float formatFromCurrencyValue(@NonNull String text) {
        text = text.trim();
        float value;
        try {
            value = Float.parseFloat(text);
        } catch (NumberFormatException ex) {
            value = 0;
        }
        return value;
    }

}

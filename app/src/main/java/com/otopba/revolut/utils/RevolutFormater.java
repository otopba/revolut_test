package com.otopba.revolut.utils;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.util.Locale;

public class RevolutFormater implements Formater {

    private final NumberFormat formatter;

    public RevolutFormater() {
        formatter = NumberFormat.getNumberInstance(Locale.GERMAN);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        formatter.setGroupingUsed(false);
    }

    @NonNull
    public String formatCurrency(double value) {
        return formatter.format(value);
    }

}

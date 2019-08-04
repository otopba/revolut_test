package com.otopba.revolut.ui.theme;

import androidx.annotation.NonNull;

public interface AppTheme {

    @NonNull
    Colors getColors();

    void invertTheme();

    boolean isDay();

}
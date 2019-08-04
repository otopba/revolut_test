package com.otopba.revolut.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;

public class Snackbars {

    private final View view;
    private Snackbar snackbar;

    public Snackbars(@NonNull View view) {
        this.view = view;
    }

    public void showShort(@StringRes int message) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}

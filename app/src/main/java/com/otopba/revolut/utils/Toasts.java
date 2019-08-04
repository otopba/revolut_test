package com.otopba.revolut.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class Toasts {

    public static void showShort(Context context, @StringRes int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}

package com.otopba.revolut.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.ironz.binaryprefs.BinaryPreferencesBuilder;
import com.ironz.binaryprefs.Preferences;

public class PrefsImpl implements Prefs {

    private static final String DAY_THEME = "DAY_THEME";

    private final Preferences preferences;

    public PrefsImpl(@NonNull Context context) {
        preferences = new BinaryPreferencesBuilder(context)
                .name("user_data")
                .build();
    }

    @Override
    public void setDayTheme(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DAY_THEME, value);
        editor.apply();
    }

    @Override
    public boolean isDayTheme() {
        return preferences.getBoolean(DAY_THEME, true);
    }

}

package com.otopba.revolut.ui.theme;

import android.graphics.Color;

import androidx.annotation.NonNull;

public class RevolutAppTheme implements AppTheme {

    private final Colors dayColors;
    private final Colors nightColors;
    private boolean day;

    public RevolutAppTheme(boolean day) {
        this.day = day;
        dayColors = new Colors.Builder()
                .setMainBackgroundColor(Color.parseColor("#ffffff"))
                .setRippleColor(Color.parseColor("#26333344"))
                .setTitleTextColor(Color.parseColor("#191C1F"))
                .setSubtitleTextColor(Color.parseColor("#8B959E"))
                .setMoonColor(Color.parseColor("#8e8e93"))
                .setHintTextColor(Color.parseColor("#84848E"))
                .setAccentColor(Color.parseColor("#3537A5"))
                .build();

        nightColors = new Colors.Builder()
                .setMainBackgroundColor(Color.parseColor("#1d2432"))
                .setRippleColor(Color.parseColor("#26D6D6D9"))
                .setMoonColor(Color.parseColor("#ffffff"))
                .setTitleTextColor(Color.parseColor("#bad1e8"))
                .setSubtitleTextColor(Color.parseColor("#cad7e3"))
                .setHintTextColor(Color.parseColor("#75757B"))
                .setAccentColor(Color.parseColor("#3537A5"))
                .build();
    }

    @Override
    @NonNull
    public Colors getColors() {
        if (day) {
            return dayColors;
        } else {
            return nightColors;
        }
    }

    @Override
    public void invertTheme() {
        day = !day;
    }

    @Override
    public boolean isDay() {
        return day;
    }

}
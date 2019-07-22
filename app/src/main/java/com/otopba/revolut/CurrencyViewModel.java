package com.otopba.revolut;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class CurrencyViewModel {

    @DrawableRes
    public final int icon;
    @StringRes
    public final int title;
    @StringRes
    public final int subtitle;
    public final double value;

    public CurrencyViewModel(@DrawableRes int icon, @StringRes int title, @StringRes int subtitle,
                             double value) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.value = value;
    }

}

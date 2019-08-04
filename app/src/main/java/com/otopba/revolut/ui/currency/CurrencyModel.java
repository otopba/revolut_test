package com.otopba.revolut.ui.currency;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.otopba.revolut.storage.Currency;
import com.otopba.revolut.R;

public class CurrencyModel {

    public final Currency currency; //TODO: убрать
    @DrawableRes
    public final int icon;
    @StringRes
    public final int title;
    @StringRes
    public final int subtitle;

    private CurrencyModel(@NonNull Currency currency, @DrawableRes int icon, @StringRes int title,
                          @StringRes int subtitle) {
        this.currency = currency;
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }

    @NonNull
    public static CurrencyModel eur() {
        return new CurrencyModel(Currency.EUR, R.drawable.ic_european_union, R.string.eur_title, R.string.eur_subtitle);
    }

    @NonNull
    public static CurrencyModel aud() {
        return new CurrencyModel(Currency.AUD, R.drawable.ic_australia, R.string.aud_title, R.string.aud_subtitle);
    }

    @NonNull
    public static CurrencyModel bgn() {
        return new CurrencyModel(Currency.BGN, R.drawable.ic_bulgaria, R.string.bgn_title, R.string.bgn_subtitle);
    }

    @NonNull
    public static CurrencyModel brl() {
        return new CurrencyModel(Currency.BRL, R.drawable.ic_brazil, R.string.brl_title, R.string.brl_subtitle);
    }

    @NonNull
    public static CurrencyModel cad() {
        return new CurrencyModel(Currency.CAD, R.drawable.ic_canada, R.string.cad_title, R.string.cad_subtitle);
    }

    @NonNull
    public static CurrencyModel chf() {
        return new CurrencyModel(Currency.CHF, R.drawable.ic_switzerland, R.string.chf_title, R.string.chf_subtitle);
    }

    @NonNull
    public static CurrencyModel cny() {
        return new CurrencyModel(Currency.CNY, R.drawable.ic_china, R.string.cny_title, R.string.cny_subtitle);
    }

    @NonNull
    public static CurrencyModel czk() {
        return new CurrencyModel(Currency.CZK, R.drawable.ic_czech_republic, R.string.czk_title, R.string.czk_subtitle);
    }

    @NonNull
    public static CurrencyModel dkk() {
        return new CurrencyModel(Currency.DKK, R.drawable.ic_denmark, R.string.dkk_title, R.string.dkk_subtitle);
    }

    @NonNull
    public static CurrencyModel gbp() {
        return new CurrencyModel(Currency.GBP, R.drawable.ic_united_kingdom, R.string.gbr_title, R.string.gbr_subtitle);
    }

    @NonNull
    public static CurrencyModel hkd() {
        return new CurrencyModel(Currency.HKD, R.drawable.ic_hong_kong, R.string.hkd_title, R.string.hkd_subtitle);
    }

    @NonNull
    public static CurrencyModel hrk() {
        return new CurrencyModel(Currency.HRK, R.drawable.ic_croatia, R.string.hrk_title, R.string.hrk_subtitle);
    }

    @NonNull
    public static CurrencyModel huf() {
        return new CurrencyModel(Currency.HUF, R.drawable.ic_hungary, R.string.huf_title, R.string.huf_subtitle);
    }

    @NonNull
    public static CurrencyModel idr() {
        return new CurrencyModel(Currency.IDR, R.drawable.ic_indonesia, R.string.idr_title, R.string.idr_subtitle);
    }

    @NonNull
    public static CurrencyModel ils() {
        return new CurrencyModel(Currency.ILS, R.drawable.ic_israel, R.string.ils_title, R.string.ils_subtitle);
    }

    @NonNull
    public static CurrencyModel inr() {
        return new CurrencyModel(Currency.INR, R.drawable.ic_india, R.string.inr_title, R.string.inr_subtitle);
    }

    @NonNull
    public static CurrencyModel isk() {
        return new CurrencyModel(Currency.ISK, R.drawable.ic_iceland, R.string.isk_title, R.string.isk_subtitle);
    }

    @NonNull
    public static CurrencyModel jpy() {
        return new CurrencyModel(Currency.JPY, R.drawable.ic_japan, R.string.jpy_title, R.string.jpy_subtitle);
    }

    @NonNull
    public static CurrencyModel krw() {
        return new CurrencyModel(Currency.KRW, R.drawable.ic_south_korea, R.string.krw_title, R.string.krw_subtitle);
    }

    @NonNull
    public static CurrencyModel mxn() {
        return new CurrencyModel(Currency.MXN, R.drawable.ic_mexico, R.string.mxn_title, R.string.mxn_subtitle);
    }

    @NonNull
    public static CurrencyModel myr() {
        return new CurrencyModel(Currency.MYR, R.drawable.ic_malaysia, R.string.myr_title, R.string.myr_subtitle);
    }

    @NonNull
    public static CurrencyModel nok() {
        return new CurrencyModel(Currency.NOK, R.drawable.ic_norway, R.string.nok_title, R.string.nok_subtitle);
    }

    @NonNull
    public static CurrencyModel nzd() {
        return new CurrencyModel(Currency.NZD, R.drawable.ic_new_zealand, R.string.nzd_title, R.string.nzd_subtitle);
    }

    @NonNull
    public static CurrencyModel php() {
        return new CurrencyModel(Currency.PHP, R.drawable.ic_philippines, R.string.php_title, R.string.php_subtitle);
    }

    @NonNull
    public static CurrencyModel pLn() {
        return new CurrencyModel(Currency.PLN, R.drawable.ic_republic_of_poland, R.string.pln_title, R.string.pln_subtitle);
    }

    @NonNull
    public static CurrencyModel ron() {
        return new CurrencyModel(Currency.RON, R.drawable.ic_romania, R.string.ron_title, R.string.ron_subtitle);
    }

    @NonNull
    public static CurrencyModel rub() {
        return new CurrencyModel(Currency.RUB, R.drawable.ic_russia, R.string.rub_title, R.string.rub_subtitle);
    }

    @NonNull
    public static CurrencyModel sek() {
        return new CurrencyModel(Currency.SEK, R.drawable.ic_sweden, R.string.sek_title, R.string.sek_subtitle);
    }

    @NonNull
    public static CurrencyModel sgd() {
        return new CurrencyModel(Currency.SGD, R.drawable.ic_singapore, R.string.sgd_title, R.string.sgd_subtitle);
    }

    @NonNull
    public static CurrencyModel thb() {
        return new CurrencyModel(Currency.THB, R.drawable.ic_thailand, R.string.thb_title, R.string.thb_subtitle);
    }

    @NonNull
    public static CurrencyModel tRY() {
        return new CurrencyModel(Currency.TRY, R.drawable.ic_turkey, R.string.try_title, R.string.try_subtitle);
    }

    @NonNull
    public static CurrencyModel usd() {
        return new CurrencyModel(Currency.USD, R.drawable.ic_united_states_of_america, R.string.usd_title, R.string.usd_subtitle);
    }

    @NonNull
    public static CurrencyModel zar() {
        return new CurrencyModel(Currency.ZAR, R.drawable.ic_south_africa, R.string.zar_title, R.string.zar_subtitle);
    }

}

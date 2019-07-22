package com.otopba.revolut;

import androidx.annotation.NonNull;

import com.otopba.revolut.utils.Formater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyViewMaper {

    private final Formater formater;
    private final Map<Currency, CurrencyViewModel> map = new HashMap<>();
    private Currency mainCurrency;

    public CurrencyViewMaper(@NonNull Formater formater) {
        this.formater = formater;
    }

    @NonNull
    public List<CurrencyViewModel> updateCurrency(@NonNull CurrencyValue currencyValue) {
        mainCurrency = currencyValue.base;
        map.put(mainCurrency, mapCurrencyToModel(mainCurrency, formater.formatCurrency(1.0)));
        for (Map.Entry<Currency, Double> entry : currencyValue.rates.entrySet()) {
            //TODO: подумать как фильтровать нулы
            Currency currency = entry.getKey();
            String value = formater.formatCurrency(entry.getValue());
            map.put(currency, mapCurrencyToModel(currency, value));
        }
        return new ArrayList<>(map.values());
    }

    @NonNull
    private CurrencyViewModel mapCurrencyToModel(@NonNull Currency currency, @NonNull String value) {
        switch (currency) {
            case EUR:
                return new CurrencyViewModel(R.drawable.ic_european_union, R.string.eur_title, R.string.eur_subtitle, value);
            case AUD:
                return new CurrencyViewModel(R.drawable.ic_australia, R.string.aud_title, R.string.aud_subtitle, value);
            case BGN:
                return new CurrencyViewModel(R.drawable.ic_bulgaria, R.string.bgn_title, R.string.bgn_subtitle, value);
            case BRL:
                return new CurrencyViewModel(R.drawable.ic_brazil, R.string.brl_title, R.string.brl_subtitle, value);
            case CAD:
                return new CurrencyViewModel(R.drawable.ic_canada, R.string.cad_title, R.string.cad_subtitle, value);
            case CHF:
                return new CurrencyViewModel(R.drawable.ic_switzerland, R.string.chf_title, R.string.chf_subtitle, value);
            case CNY:
                return new CurrencyViewModel(R.drawable.ic_china, R.string.cny_title, R.string.cny_subtitle, value);
            case CZK:
                return new CurrencyViewModel(R.drawable.ic_czech_republic, R.string.czk_title, R.string.czk_subtitle, value);
            case DKK:
                return new CurrencyViewModel(R.drawable.ic_denmark, R.string.dkk_title, R.string.dkk_subtitle, value);
            case GBP:
                return new CurrencyViewModel(R.drawable.ic_united_kingdom, R.string.gbr_title, R.string.gbr_subtitle, value);
            case HKD:
                return new CurrencyViewModel(R.drawable.ic_hong_kong, R.string.hkd_title, R.string.hkd_subtitle, value);
            case HRK:
                return new CurrencyViewModel(R.drawable.ic_croatia, R.string.hrk_title, R.string.hrk_subtitle, value);
            case HUF:
                return new CurrencyViewModel(R.drawable.ic_hungary, R.string.huf_title, R.string.huf_subtitle, value);
            case IDR:
                return new CurrencyViewModel(R.drawable.ic_indonesia, R.string.idr_title, R.string.idr_subtitle, value);
            case ILS:
                return new CurrencyViewModel(R.drawable.ic_israel, R.string.ils_title, R.string.ils_subtitle, value);
            case INR:
                return new CurrencyViewModel(R.drawable.ic_india, R.string.inr_title, R.string.inr_subtitle, value);
            case ISK:
                return new CurrencyViewModel(R.drawable.ic_iceland, R.string.isk_title, R.string.isk_subtitle, value);
            case JPY:
                return new CurrencyViewModel(R.drawable.ic_japan, R.string.jpy_title, R.string.jpy_subtitle, value);
            case KRW:
                return new CurrencyViewModel(R.drawable.ic_south_korea, R.string.krw_title, R.string.krw_subtitle, value);
            case MXN:
                return new CurrencyViewModel(R.drawable.ic_mexico, R.string.mxn_title, R.string.mxn_subtitle, value);
            case MYR:
                return new CurrencyViewModel(R.drawable.ic_malaysia, R.string.myr_title, R.string.myr_subtitle, value);
            case NOK:
                return new CurrencyViewModel(R.drawable.ic_norway, R.string.nok_title, R.string.nok_subtitle, value);
            case NZD:
                return new CurrencyViewModel(R.drawable.ic_new_zealand, R.string.nzd_title, R.string.nzd_subtitle, value);
            case PHP:
                return new CurrencyViewModel(R.drawable.ic_philippines, R.string.php_title, R.string.php_subtitle, value);
            case PLN:
                return new CurrencyViewModel(R.drawable.ic_republic_of_poland, R.string.pln_title, R.string.pln_subtitle, value);
            case RON:
                return new CurrencyViewModel(R.drawable.ic_romania, R.string.ron_title, R.string.ron_subtitle, value);
            case RUB:
                return new CurrencyViewModel(R.drawable.ic_russia, R.string.rub_title, R.string.rub_subtitle, value);
            case SEK:
                return new CurrencyViewModel(R.drawable.ic_sweden, R.string.sek_title, R.string.sek_subtitle, value);
            case SGD:
                return new CurrencyViewModel(R.drawable.ic_singapore, R.string.sgd_title, R.string.sgd_subtitle, value);
            case THB:
                return new CurrencyViewModel(R.drawable.ic_thailand, R.string.thb_title, R.string.thb_subtitle, value);
            case TRY:
                return new CurrencyViewModel(R.drawable.ic_turkey, R.string.try_title, R.string.try_subtitle, value);
            case USD:
                return new CurrencyViewModel(R.drawable.ic_united_states_of_america, R.string.usd_title, R.string.usd_subtitle, value);
            case ZAR:
                return new CurrencyViewModel(R.drawable.ic_south_africa, R.string.zar_title, R.string.zar_subtitle, value);
        }
        throw new RuntimeException("Unknown currency inside enum");
    }


}

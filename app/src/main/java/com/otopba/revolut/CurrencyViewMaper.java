package com.otopba.revolut;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CurrencyViewMaper {

    private final Map<Currency, CurrencyViewModel> map = new HashMap<>();

    @NonNull
    public List<CurrencyViewModel> updateCurrency(@NonNull CurrencyValue currencyValue) {
        Iterator<Currency> currencyIterator = map.keySet().iterator();
        while (currencyIterator.hasNext()) {
            Currency currency = currencyIterator.next();
            if (!currencyValue.rates.containsKey(currency)) {
                currencyIterator.remove();
            }
        }

        for (Map.Entry<Currency, Double> entry : currencyValue.rates.entrySet()) {
            //TODO: подумать как фильтровать нулы
            Currency currency = entry.getKey();
            CurrencyViewModel currencyViewModel = map.get(currency);
            if (currencyViewModel == null) {
                currencyViewModel = mapCurrencyToModel(currency);
                map.put(currency, currencyViewModel);
            } else {
                //TODO: обновить курс
            }
        }

        return new ArrayList<>(map.values());
    }

    @NonNull
    private CurrencyViewModel mapCurrencyToModel(@NonNull Currency currency) {
        switch (currency) {
            case EUR:
                return new CurrencyViewModel(R.drawable.ic_european_union, R.string.eur_title, R.string.eur_subtitle, 0);
            case AUD:
                return new CurrencyViewModel(R.drawable.ic_australia, R.string.aud_title, R.string.aud_subtitle, 0);
            case BGN:
                return new CurrencyViewModel(R.drawable.ic_bulgaria, R.string.bgn_title, R.string.bgn_subtitle, 0);
            case BRL:
                return new CurrencyViewModel(R.drawable.ic_brazil, R.string.brl_title, R.string.brl_subtitle, 0);
            case CAD:
                return new CurrencyViewModel(R.drawable.ic_canada, R.string.cad_title, R.string.cad_subtitle, 0);
            case CHF:
                return new CurrencyViewModel(R.drawable.ic_switzerland, R.string.chf_title, R.string.chf_subtitle, 0);
            case CNY:
                return new CurrencyViewModel(R.drawable.ic_china, R.string.cny_title, R.string.cny_subtitle, 0);
            case CZK:
                return new CurrencyViewModel(R.drawable.ic_czech_republic, R.string.czk_title, R.string.czk_subtitle, 0);
            case DKK:
                return new CurrencyViewModel(R.drawable.ic_denmark, R.string.dkk_title, R.string.dkk_subtitle, 0);
            case GBP:
                return new CurrencyViewModel(R.drawable.ic_united_kingdom, R.string.gbr_title, R.string.gbr_subtitle, 0);
            case HKD:
                return new CurrencyViewModel(R.drawable.ic_hong_kong, R.string.hkd_title, R.string.hkd_subtitle, 0);
            case HRK:
                return new CurrencyViewModel(R.drawable.ic_croatia, R.string.hrk_title, R.string.hrk_subtitle, 0);
            case HUF:
                return new CurrencyViewModel(R.drawable.ic_hungary, R.string.huf_title, R.string.huf_subtitle, 0);
            case IDR:
                return new CurrencyViewModel(R.drawable.ic_indonesia, R.string.idr_title, R.string.idr_subtitle, 0);
            case ILS:
                return new CurrencyViewModel(R.drawable.ic_israel, R.string.ils_title, R.string.ils_subtitle, 0);
            case INR:
                return new CurrencyViewModel(R.drawable.ic_india, R.string.inr_title, R.string.inr_subtitle, 0);
            case ISK:
                return new CurrencyViewModel(R.drawable.ic_iceland, R.string.isk_title, R.string.isk_subtitle, 0);
            case JPY:
                return new CurrencyViewModel(R.drawable.ic_japan, R.string.jpy_title, R.string.jpy_subtitle, 0);
            case KRW:
                return new CurrencyViewModel(R.drawable.ic_south_korea, R.string.krw_title, R.string.krw_subtitle, 0);
            case MXN:
                return new CurrencyViewModel(R.drawable.ic_mexico, R.string.mxn_title, R.string.mxn_subtitle, 0);
            case MYR:
                return new CurrencyViewModel(R.drawable.ic_malaysia, R.string.myr_title, R.string.myr_subtitle, 0);
            case NOK:
                return new CurrencyViewModel(R.drawable.ic_norway, R.string.nok_title, R.string.nok_subtitle, 0);
            case NZD:
                return new CurrencyViewModel(R.drawable.ic_new_zealand, R.string.nzd_title, R.string.nzd_subtitle, 0);
            case PHP:
                return new CurrencyViewModel(R.drawable.ic_philippines, R.string.php_title, R.string.php_subtitle, 0);
            case PLN:
                return new CurrencyViewModel(R.drawable.ic_republic_of_poland, R.string.pln_title, R.string.pln_subtitle, 0);
            case RON:
                return new CurrencyViewModel(R.drawable.ic_romania, R.string.ron_title, R.string.ron_subtitle, 0);
            case RUB:
                return new CurrencyViewModel(R.drawable.ic_russia, R.string.rub_title, R.string.rub_subtitle, 0);
            case SEK:
                return new CurrencyViewModel(R.drawable.ic_sweden, R.string.sek_title, R.string.sek_subtitle, 0);
            case SGD:
                return new CurrencyViewModel(R.drawable.ic_singapore, R.string.sgd_title, R.string.sgd_subtitle, 0);
            case THB:
                return new CurrencyViewModel(R.drawable.ic_thailand, R.string.thb_title, R.string.thb_subtitle, 0);
            case TRY:
                return new CurrencyViewModel(R.drawable.ic_turkey, R.string.try_title, R.string.try_subtitle, 0);
            case USD:
                return new CurrencyViewModel(R.drawable.ic_united_states_of_america, R.string.usd_title, R.string.usd_subtitle, 0);
            case ZAR:
                return new CurrencyViewModel(R.drawable.ic_south_africa, R.string.zar_title, R.string.zar_subtitle, 0);
        }
        throw new RuntimeException("Unknown currency inside enum");
    }


}

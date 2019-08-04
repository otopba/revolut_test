package com.otopba.revolut.ui.currency;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otopba.revolut.Currency;
import com.otopba.revolut.utils.Formater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyAdapterState {

    private final Map<Currency, CurrencyValue> map = new HashMap<>();
    private final Formater formater;
    private final Comparator<CurrencyValue> comparator;

    private Currency mainCurrency;

    public CurrencyAdapterState(@NonNull Formater formater) {
        this.formater = formater;
        comparator = (currencyValue1, currencyValue2) -> {
            if (mainCurrency == null) {
                return 0;
            }
            if (currencyValue1.getModel().currency == mainCurrency) {
                return -1;
            }
            if (currencyValue2.getModel().currency == mainCurrency) {
                return 1;
            }
            return 0;
        };
    }

    public void update(@NonNull Map<Currency, Float> values, @Nullable Currency mainCurrency) {
        this.mainCurrency = mainCurrency;
        for (Map.Entry<Currency, Float> entry : values.entrySet()) {
            update(entry.getKey(), entry.getValue());
        }
    }

    private void update(@NonNull Currency currency, float value) {
        CurrencyValue currencyValue = map.get(currency);
        String formatedValue = formater.formatToCurrencyValue(value);
        if (currencyValue == null) {
            currencyValue = createNewCurrencyValue(currency, formater.formatToCurrencyValue(value));
            map.put(currency, currencyValue);
        } else {
            currencyValue.setValue(formatedValue);
        }
    }

    @NonNull
    public List<CurrencyValue> getValues() {
        List<CurrencyValue> result = new ArrayList<>(map.values());
        if (mainCurrency != null) {
            Collections.sort(result, comparator);
        }
        return result;
    }

    @NonNull
    private CurrencyValue createNewCurrencyValue(@NonNull Currency currency, @NonNull String value) {
        switch (currency) {
            case EUR:
                return new CurrencyValue(CurrencyModel.eur(), value);
            case AUD:
                return new CurrencyValue(CurrencyModel.aud(), value);
            case BGN:
                return new CurrencyValue(CurrencyModel.bgn(), value);
            case BRL:
                return new CurrencyValue(CurrencyModel.brl(), value);
            case CAD:
                return new CurrencyValue(CurrencyModel.cad(), value);
            case CHF:
                return new CurrencyValue(CurrencyModel.chf(), value);
            case CNY:
                return new CurrencyValue(CurrencyModel.cny(), value);
            case CZK:
                return new CurrencyValue(CurrencyModel.czk(), value);
            case DKK:
                return new CurrencyValue(CurrencyModel.dkk(), value);
            case GBP:
                return new CurrencyValue(CurrencyModel.gbp(), value);
            case HKD:
                return new CurrencyValue(CurrencyModel.hkd(), value);
            case HRK:
                return new CurrencyValue(CurrencyModel.hrk(), value);
            case HUF:
                return new CurrencyValue(CurrencyModel.huf(), value);
            case IDR:
                return new CurrencyValue(CurrencyModel.idr(), value);
            case ILS:
                return new CurrencyValue(CurrencyModel.ils(), value);
            case INR:
                return new CurrencyValue(CurrencyModel.inr(), value);
            case ISK:
                return new CurrencyValue(CurrencyModel.isk(), value);
            case JPY:
                return new CurrencyValue(CurrencyModel.jpy(), value);
            case KRW:
                return new CurrencyValue(CurrencyModel.krw(), value);
            case MXN:
                return new CurrencyValue(CurrencyModel.mxn(), value);
            case MYR:
                return new CurrencyValue(CurrencyModel.myr(), value);
            case NOK:
                return new CurrencyValue(CurrencyModel.nok(), value);
            case NZD:
                return new CurrencyValue(CurrencyModel.nzd(), value);
            case PHP:
                return new CurrencyValue(CurrencyModel.php(), value);
            case PLN:
                return new CurrencyValue(CurrencyModel.pLn(), value);
            case RON:
                return new CurrencyValue(CurrencyModel.ron(), value);
            case RUB:
                return new CurrencyValue(CurrencyModel.rub(), value);
            case SEK:
                return new CurrencyValue(CurrencyModel.sek(), value);
            case SGD:
                return new CurrencyValue(CurrencyModel.sgd(), value);
            case THB:
                return new CurrencyValue(CurrencyModel.thb(), value);
            case TRY:
                return new CurrencyValue(CurrencyModel.tRY(), value);
            case USD:
                return new CurrencyValue(CurrencyModel.usd(), value);
            case ZAR:
                return new CurrencyValue(CurrencyModel.zar(), value);
        }
        throw new RuntimeException("Unknown currency inside enum");
    }

}

package com.otopba.revolut.ui.currency;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.otopba.revolut.storage.Currency;

import java.util.List;

public class CurrencyDiffUtilCallback extends DiffUtil.Callback {

    private final List<CurrencyValue> oldValues;
    private final List<CurrencyValue> newValues;
    private final Currency oldMainCurrency;
    private final Currency newMainCurrency;

    public CurrencyDiffUtilCallback(@NonNull List<CurrencyValue> oldValues, @NonNull List<CurrencyValue> newValues,
                                    @Nullable Currency oldMainCurrency, @Nullable Currency newMainCurrency) {
        this.oldValues = oldValues;
        this.newValues = newValues;
        this.oldMainCurrency = oldMainCurrency;
        this.newMainCurrency = newMainCurrency;
    }

    @Override
    public int getOldListSize() {
        return oldValues.size();
    }

    @Override
    public int getNewListSize() {
        return newValues.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        CurrencyValue oldValue = oldValues.get(oldItemPosition);
        CurrencyValue newValue = newValues.get(newItemPosition);
        return oldValue.model.currency == newValue.model.currency;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        CurrencyValue oldValue = oldValues.get(oldItemPosition);
        CurrencyValue newValue = newValues.get(newItemPosition);
        boolean isOldMain = oldValue.model.currency == oldMainCurrency;
        boolean isNewMain = newValue.model.currency == newMainCurrency;
        if (isOldMain && isNewMain) {
            return true;
        }
        return oldValue.model.currency == newValue.model.currency && TextUtils.equals(oldValue.value, newValue.value) && isOldMain == isNewMain;
    }

}

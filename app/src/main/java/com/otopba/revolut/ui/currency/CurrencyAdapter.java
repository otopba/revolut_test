package com.otopba.revolut.ui.currency;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.otopba.revolut.R;
import com.otopba.revolut.storage.Currency;
import com.otopba.revolut.ui.SimpleTextWatcher;
import com.otopba.revolut.ui.theme.AppTheme;
import com.otopba.revolut.ui.theme.Colored;
import com.otopba.revolut.ui.theme.Colors;
import com.otopba.revolut.utils.KeyboardUtils;
import com.otopba.revolut.utils.ThemeUtils;

import java.util.List;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> {

    private final LayoutInflater layoutInflater;
    private final AppTheme appTheme;
    private final Subject<Currency> clickSubject = PublishSubject.create();
    private final Subject<CharSequence> valueSubject = PublishSubject.create();
    private List<CurrencyValue> values;
    private Currency selectedCurrency;

    CurrencyAdapter(@NonNull LayoutInflater layoutInflater, @NonNull AppTheme appTheme, @NonNull List<CurrencyValue> values) {
        this.layoutInflater = layoutInflater;
        this.appTheme = appTheme;
        this.values = values;
    }

    void updateCurrencies(@NonNull List<CurrencyValue> currencies, @Nullable Currency selectedCurrency) {
        this.values = currencies;
        this.selectedCurrency = selectedCurrency;
    }

    @NonNull
    @Override
    public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrencyHolder(layoutInflater.inflate(R.layout.currency, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyHolder holder, int position) {
        holder.bind(values.get(position));
    }

    @Override
    public long getItemId(int position) {
        return values.get(position).model.currency.ordinal();
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public Subject<Currency> getClickSubject() {
        return clickSubject;
    }

    public Subject<CharSequence> getValueSubject() {
        return valueSubject;
    }

    class CurrencyHolder extends RecyclerView.ViewHolder implements Colored {

        private View rootView;
        private ImageView iconView;
        private TextView titleView;
        private TextView subtitleView;
        private EditText valueView;
        private CurrencyValue currencyValue;
        private boolean isDayTheme;

        CurrencyHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            iconView = itemView.findViewById(R.id.currency__icon);
            titleView = itemView.findViewById(R.id.currency__title);
            subtitleView = itemView.findViewById(R.id.currency__subtitle);
            valueView = itemView.findViewById(R.id.currency__value);
            itemView.setOnClickListener(v -> handleClick());
            valueView.setOnFocusChangeListener((v, hasFocus) -> onFicusChange(hasFocus));
            valueView.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    CurrencyHolder.this.onTextChanged(s);
                }
            });
            applyColors(appTheme.getColors());
            isDayTheme = appTheme.isDay();
        }

        private void onFicusChange(boolean hasFocus) {
            if (hasFocus) {
                handleClick();
            }
        }

        private void onTextChanged(CharSequence text) {
            if (TextUtils.equals(currencyValue.value, text)) {
                return;
            }
            handleTextChange(text);
        }

        private void handleClick() {
            if (currencyValue != null) {
                clickSubject.onNext(currencyValue.model.currency);
            }
        }

        private void handleTextChange(@Nullable CharSequence text) {
            if (text == null) {
                text = "";
            }
            valueSubject.onNext(text);
        }

        void bind(@NonNull CurrencyValue value) {
            if (currencyValue == null || currencyValue.model.currency != value.model.currency) {
                iconView.setImageResource(value.model.icon);
                titleView.setText(value.model.title);
                subtitleView.setText(value.model.subtitle);
            }
            this.currencyValue = value;
            if (selectedCurrency != null && currencyValue.model.currency == selectedCurrency) {
                if (!valueView.hasFocus()) {
                    valueView.requestFocus();
                    valueView.setSelection(valueView.length());
                    KeyboardUtils.showKeyboard(valueView);
                }
            } else {
                valueView.setText(value.value);
            }
            if (isDayTheme != appTheme.isDay()) {
                applyColors(appTheme.getColors());
                isDayTheme = appTheme.isDay();
            }
        }

        @Override
        public void applyColors(@NonNull Colors colors) {
            rootView.setBackground(ThemeUtils.recyclerItemBackground(colors.rippleColor));
            titleView.setTextColor(colors.titleTextColor);
            subtitleView.setTextColor(colors.subtitleTextColor);
            valueView.setTextColor(colors.titleTextColor);
            valueView.setHintTextColor(colors.hintTextColor);
        }
    }

}



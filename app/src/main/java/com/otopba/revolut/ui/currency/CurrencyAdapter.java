package com.otopba.revolut.ui.currency;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.otopba.revolut.Currency;
import com.otopba.revolut.R;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> {

    private final LayoutInflater layoutInflater;
    private List<CurrencyValue> values;
    private Currency selectedCurrency;
    private Listener listener;

    public CurrencyAdapter(@NonNull LayoutInflater layoutInflater, @NonNull List<CurrencyValue> values,
                           @NonNull Listener listener) {
        this.layoutInflater = layoutInflater;
        this.values = values;
        this.listener = listener;
    }

    public void updateCurrencies(@NonNull List<CurrencyValue> currencies, @Nullable Currency selectedCurrency) {
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
        return values.get(position).getModel().currency.ordinal();
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class CurrencyHolder extends RecyclerView.ViewHolder {

        private ImageView iconView;
        private TextView titleView;
        private TextView subtitleView;
        private EditText valueView;

        private Currency currency;

        public CurrencyHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.currency__icon);
            titleView = itemView.findViewById(R.id.currency__title);
            subtitleView = itemView.findViewById(R.id.currency__subtitle);
            valueView = itemView.findViewById(R.id.currency__value);
            itemView.setOnClickListener(v -> handleClick());
            valueView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    handleTextChange(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        private void handleClick() {
            if (currency != null) {
                listener.onCurrencyClick(currency);
            }
        }

        private void handleTextChange(@Nullable CharSequence text) {
            listener.onCurrencyValueChanged(text);
        }

        public void bind(@NonNull CurrencyValue value) {
            CurrencyModel model = value.getModel();
            this.currency = value.getModel().currency;
            iconView.setImageResource(model.icon);
            titleView.setText(model.title);
            subtitleView.setText(model.subtitle);
            valueView.setText(value.getValue());
            if (selectedCurrency != null && currency == selectedCurrency) {
                valueView.requestFocus();
                valueView.setSelection(valueView.length());//TODO: делать 1 раз
            }
        }
    }

    public interface Listener {

        void onCurrencyClick(@NonNull Currency currency);

        void onCurrencyValueChanged(@Nullable CharSequence text);

    }

}



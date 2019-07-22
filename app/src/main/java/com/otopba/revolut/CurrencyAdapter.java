package com.otopba.revolut;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> {

    private final LayoutInflater layoutInflater;
    private List<CurrencyViewModel> currencies;

    public CurrencyAdapter(@NonNull LayoutInflater layoutInflater, @NonNull List<CurrencyViewModel> currencies) {
        this.layoutInflater = layoutInflater;
        this.currencies = currencies;
    }

    public void updateCurrencies(@NonNull List<CurrencyViewModel> currencies) {
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrencyHolder(layoutInflater.inflate(R.layout.currency, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyHolder holder, int position) {
        holder.bind(currencies.get(position));
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    static class CurrencyHolder extends RecyclerView.ViewHolder {

        private ImageView iconView;
        private TextView titleView;
        private TextView subtitleView;
        private EditText valueView;

        public CurrencyHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.currency__icon);
            titleView = itemView.findViewById(R.id.currency__title);
            subtitleView = itemView.findViewById(R.id.currency__subtitle);
            valueView = itemView.findViewById(R.id.currency__value);
        }

        public void bind(@NonNull CurrencyViewModel currency) {
            iconView.setImageResource(currency.icon);
            titleView.setText(currency.title);
            subtitleView.setText(currency.subtitle);
            valueView.setText(String.valueOf(currency.value));
        }

    }

}



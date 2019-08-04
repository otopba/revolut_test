package com.otopba.revolut.ui.currency;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.otopba.revolut.App;
import com.otopba.revolut.Currency;
import com.otopba.revolut.R;
import com.otopba.revolut.controller.CurrencyController;
import com.otopba.revolut.utils.Formatter;
import com.otopba.revolut.utils.Toasts;

import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class CurrencyFragment extends Fragment implements CurrencyAdapter.Listener, CurrencyController.Listener {

    public static final String TAG = CurrencyFragment.class.getName();

    @Inject
    Formatter formatter;
    @Inject
    CurrencyController currencyController;

    private CurrencyAdapterState currencyAdapterState;
    private RecyclerView currenciesView;
    private CurrencyAdapter currencyAdapter;
    private LottieAnimationView loadingView;

    public static CurrencyFragment newInstance() {
        return new CurrencyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();
        if (activity == null) {
            throw new RuntimeException("Activity is null");
        }
        ((App) activity.getApplication()).getAppComponent().inject(this);
        currencyAdapterState = new CurrencyAdapterState(formatter);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency, container, false);
        currenciesView = view.findViewById(R.id.fragment_currency__currencies);
        loadingView = view.findViewById(R.id.fragment_currency__loading);
        setupRecyclerView(inflater);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupVisibility();
        currencyController.registerListener(this);
    }

    private void setupVisibility() {
        if (currencyAdapterState.isEmpty()) {
            loadingView.setVisibility(View.VISIBLE);
            currenciesView.setVisibility(View.GONE);
        } else {
            loadingView.setVisibility(View.GONE);
            currenciesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        currencyController.unregisterListener(this);
    }

    private void setupRecyclerView(@NonNull LayoutInflater inflater) {
        currencyAdapter = new CurrencyAdapter(inflater, Collections.emptyList(), this);
        currencyAdapter.setHasStableIds(true);
        currenciesView.setAdapter(currencyAdapter);
        currenciesView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCurrencyClick(@NonNull Currency currency) {
        currencyController.setMainCurrency(currency);
    }

    @Override
    public void onCurrencyValueChanged(@Nullable CharSequence text) {
        float value;
        if (TextUtils.isEmpty(text)) {
            value = 0;
        } else {
            value = formatter.formatFromCurrencyValue(text.toString());
        }
        currencyController.setMainCurrencyValue(value);
    }

    private void setLastUpdateTime(@Nullable String lastUpdateTime) {
        if (TextUtils.isEmpty(lastUpdateTime)) {
            setTitle(null);
        } else {
            setTitle(getString(R.string.last_update, lastUpdateTime));
        }
    }

    private void setTitle(@Nullable String title) {
        Activity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, "Can't get activity");
            return;
        }
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "ActionBar is null");
            return;
        }
        actionBar.setTitle(title);
    }

    @Override
    public void onUpdate(@NonNull Map<Currency, Float> values, @Nullable Currency mainCurrency, long date) {
        currencyAdapterState.update(values, mainCurrency);
        currencyAdapter.updateCurrencies(currencyAdapterState.getValues(), mainCurrency);
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            currencyAdapter.notifyDataSetChanged();
            setLastUpdateTime(formatter.formatDate(date));
            setupVisibility();
        });
    }

    @Override
    public void onError() {
        Log.e(TAG, "Can't update currencies");
        AndroidSchedulers.mainThread().scheduleDirect(() -> Toasts.showShort(getContext(), R.string.api_error));
    }
}

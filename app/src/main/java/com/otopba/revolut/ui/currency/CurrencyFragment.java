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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.airbnb.lottie.LottieAnimationView;
import com.otopba.revolut.App;
import com.otopba.revolut.R;
import com.otopba.revolut.controller.ControllerUpdate;
import com.otopba.revolut.controller.CurrencyController;
import com.otopba.revolut.controller.error.CurrencyError;
import com.otopba.revolut.controller.error.NoConnectionError;
import com.otopba.revolut.storage.Currency;
import com.otopba.revolut.utils.Formatter;
import com.otopba.revolut.utils.Snackbars;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyFragment extends Fragment {

    public static final String TAG = CurrencyFragment.class.getName();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    Formatter formatter;
    @Inject
    CurrencyController currencyController;

    private CurrencyAdapterState currencyAdapterState;
    private RecyclerView currenciesView;
    private CurrencyAdapter currencyAdapter;
    private LottieAnimationView loadingView;
    private Snackbars snackbars;

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
        snackbars = new Snackbars(currenciesView);
        setupRecyclerView(inflater);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupVisibility();
        currencyController.start();
        Disposable errorDisposable = currencyController.getErrorSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::onError, this::onError);
        Disposable updateDisposable = currencyController.getUpdateSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::onUpdate, this::onError);
        disposables.addAll(errorDisposable, updateDisposable);
    }

    private void setupVisibility() {
        if (currencyAdapterState.isEmpty()) {
            loadingView.setVisibility(View.VISIBLE);
            currenciesView.setVisibility(View.GONE);
            loadingView.animate();
        } else {
            loadingView.setVisibility(View.GONE);
            loadingView.cancelAnimation();
            currenciesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currencyController != null) {
            currencyController.stop();
        }
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
        if (loadingView != null) {
            loadingView.cancelAnimation();
        }
    }

    private void setupRecyclerView(@NonNull LayoutInflater inflater) {
        currencyAdapter = new CurrencyAdapter(inflater, Collections.emptyList());
        currencyAdapter.setHasStableIds(true);
        Disposable clickDisposable = currencyAdapter.getClickSubject()
                .subscribeOn(Schedulers.computation())
                .distinctUntilChanged()
                .subscribe(this::onCurrencyClick, this::onError);
        Disposable valueDisposable = currencyAdapter.getValueSubject()
                .subscribeOn(Schedulers.computation())
                .subscribe(this::onCurrencyValueChanged, this::onError);
        disposables.addAll(clickDisposable, valueDisposable);
        currenciesView.setAdapter(currencyAdapter);
        currenciesView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemAnimator itemAnimator = currenciesView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
    }

    private void onCurrencyClick(@NonNull Currency currency) {
        currencyController.setMainCurrency(currency);
        currenciesView.scrollToPosition(0);
    }

    private void onCurrencyValueChanged(@Nullable CharSequence text) {
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

    private void onUpdate(@NonNull ControllerUpdate controllerUpdate) {
        Log.d(TAG, String.format("Controller update %s", controllerUpdate));
        List<CurrencyValue> oldValue = currencyAdapterState.getValues();
        Currency oldMainCurrency = currencyAdapterState.getMainCurrency();

        currencyAdapterState.update(controllerUpdate.values, controllerUpdate.mainCurrency);
        List<CurrencyValue> newValues = currencyAdapterState.getValues();
        Currency newMainCurrency = currencyAdapterState.getMainCurrency();

        CurrencyDiffUtilCallback callback = new CurrencyDiffUtilCallback(oldValue, newValues, oldMainCurrency, newMainCurrency);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(callback);
        currencyAdapter.updateCurrencies(currencyAdapterState.getValues(), controllerUpdate.mainCurrency);
        productDiffResult.dispatchUpdatesTo(currencyAdapter);
        setLastUpdateTime(formatter.formatDate(controllerUpdate.date));
        setupVisibility();
    }

    private void onError(@NonNull CurrencyError currencyError) {
        Log.e(TAG, String.format("Can't update currencies: %s", currencyError));
        if (getContext() == null) {
            return;
        }
        if (snackbars == null) {
            return;
        }
        if (currencyError instanceof NoConnectionError) {
            snackbars.showShort(R.string.no_connection);
        } else {
            snackbars.showShort(R.string.cant_update);
        }
    }

    private void onError(@NonNull Throwable throwable) {
        Log.e(TAG, "App error", throwable);
    }

}

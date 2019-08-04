package com.otopba.revolut.ui.currency;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
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
import com.otopba.revolut.preferences.Prefs;
import com.otopba.revolut.storage.Currency;
import com.otopba.revolut.ui.theme.AppTheme;
import com.otopba.revolut.ui.theme.Colored;
import com.otopba.revolut.ui.theme.Colors;
import com.otopba.revolut.utils.Formatter;
import com.otopba.revolut.utils.KeyboardUtils;
import com.otopba.revolut.utils.Snackbars;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyFragment extends Fragment implements Colored, MenuItem.OnMenuItemClickListener {

    public static final String TAG = CurrencyFragment.class.getName();
    private static final int NIGHT_MODE_MENU_ITEM = 1;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    Formatter formatter;
    @Inject
    CurrencyController currencyController;
    @Inject
    AppTheme appTheme;
    @Inject
    Prefs prefs;

    private View rootView;
    private Toolbar toolbar;
    private RecyclerView currenciesView;
    private LottieAnimationView loadingView;
    private Drawable moonDrawable;

    private CurrencyAdapter currencyAdapter;
    private CurrencyAdapterState currencyAdapterState;
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
        rootView = inflater.inflate(R.layout.fragment_currency, container, false);
        toolbar = rootView.findViewById(R.id.fragment_currency__toolbar);
        currenciesView = rootView.findViewById(R.id.fragment_currency__currencies);
        loadingView = rootView.findViewById(R.id.fragment_currency__loading);
        snackbars = new Snackbars(currenciesView);
        moonDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_moon, null);
        setHasOptionsMenu(true);
        setupToolbar();
        setupRecyclerView(inflater);
        applyColors(appTheme.getColors(), appTheme.isDay());
        return rootView;
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
        currencyAdapter = new CurrencyAdapter(inflater, appTheme, Collections.emptyList());
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
        RecyclerView.ItemAnimator itemAnimator = currenciesView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        currenciesView.setLayoutManager(linearLayoutManager);
        currenciesView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                CurrencyFragment.this.onScrolled(linearLayoutManager);
            }
        });
    }

    private void onScrolled(LinearLayoutManager linearLayoutManager) {
        if (getActivity() == null || linearLayoutManager.findFirstVisibleItemPosition() <= 0) {
            return;
        }
        KeyboardUtils.hideKeyBoard(getActivity());
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
            toolbar.setTitle(R.string.app_name);
        } else {
            toolbar.setTitle(getString(R.string.last_update, lastUpdateTime));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, NIGHT_MODE_MENU_ITEM, Menu.NONE, "")
                .setIcon(moonDrawable)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupToolbar() {
        Activity activity = getActivity();
        if (!(activity instanceof AppCompatActivity)) {
            return;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        appCompatActivity.setSupportActionBar(toolbar);
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

    @Override
    public void applyColors(@NonNull Colors colors, boolean isDay) {
        setStatusBarColor(colors.mainBackgroundColor, isDay);
        rootView.setBackgroundColor(colors.mainBackgroundColor);
        currenciesView.setBackgroundColor(Color.TRANSPARENT);
        if (currencyAdapter != null) {
            currencyAdapter.notifyDataSetChanged();
        }
        toolbar.setBackgroundColor(colors.mainBackgroundColor);
        toolbar.setTitleTextColor(colors.titleTextColor);
        moonDrawable.setColorFilter(colors.moonColor, PorterDuff.Mode.SRC_IN);

    }

    private void setStatusBarColor(int color, boolean isDay) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decorView = window.getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        if (isDay) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decorView.setSystemUiVisibility(systemUiVisibility);
        window.setStatusBarColor(color);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == NIGHT_MODE_MENU_ITEM) {
            appTheme.invertTheme();
            prefs.setDayTheme(appTheme.isDay());
            applyColors(appTheme.getColors(), appTheme.isDay());
        }
        return false;
    }
}

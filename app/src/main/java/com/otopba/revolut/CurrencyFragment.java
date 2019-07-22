package com.otopba.revolut;

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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CurrencyFragment extends Fragment {

    public static final String TAG = CurrencyFragment.class.getName();
    private final BehaviorSubject<List<CurrencyViewModel>> currencySubject = BehaviorSubject.create();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final CurrencyViewMaper currencyViewMaper = new CurrencyViewMaper();

    @Inject
    CurrencyProvider api;

    private RecyclerView currenciesView;
    private CurrencyAdapter currencyAdapter;

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
        Disposable disposable = api.getCurrency().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(currencyValue -> setLastUpdateTime(currencyValue.date))
                .map(currencyViewMaper::updateCurrency)
                .subscribe(currencySubject::onNext);//TODO: обработать ошибки
        compositeDisposable.add(disposable);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency, container, false);
        currenciesView = view.findViewById(R.id.fragment_currency__currencies);
        setupRecyclerView(inflater);
        Disposable disposable = currencySubject
                .subscribeOn(Schedulers.io())
                .subscribe(currencyViewModels -> {
                    currencyAdapter.updateCurrencies(currencyViewModels);
                    currencyAdapter.notifyDataSetChanged();//TODO: упросить
                });
        compositeDisposable.add(disposable);
        return view;
    }

    private void setupRecyclerView(@NonNull LayoutInflater inflater) {
        currencyAdapter = new CurrencyAdapter(inflater, Collections.emptyList());
        currenciesView.setAdapter(currencyAdapter);
        currenciesView.setLayoutManager(new LinearLayoutManager(getContext()));
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

}

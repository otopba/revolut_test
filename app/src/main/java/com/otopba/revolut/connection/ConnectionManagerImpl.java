package com.otopba.revolut.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class ConnectionManagerImpl implements ConnectionManager {

    private final BehaviorSubject<Boolean> connectionSubject = BehaviorSubject.create();
    private ConnectivityManager connectivityManager;

    public ConnectionManagerImpl(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        registerReceiver(context);
    }

    private void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(new NetworkChangesReceiver(), filter);
    }

    private void onNetworkChange() {
        Boolean oldValue = connectionSubject.getValue();
        boolean newValue = isConnectedToNetwork();
        if (oldValue == null || oldValue != newValue) {
            connectionSubject.onNext(newValue);
        }
    }

    private boolean isConnectedToNetwork() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @NonNull
    @Override
    public Subject<Boolean> getConnectionSubject() {
        return connectionSubject;
    }

    private class NetworkChangesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onNetworkChange();
        }

    }

}

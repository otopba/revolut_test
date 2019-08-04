package com.otopba.revolut;

import android.app.Application;

import com.otopba.revolut.dependency.AppComponent;
import com.otopba.revolut.dependency.AppModule;
import com.otopba.revolut.dependency.DaggerAppComponent;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}

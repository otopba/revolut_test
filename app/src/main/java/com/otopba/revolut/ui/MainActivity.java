package com.otopba.revolut.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.otopba.revolut.ui.currency.CurrencyFragment;
import com.otopba.revolut.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_main__container, CurrencyFragment.newInstance(), CurrencyFragment.TAG)
                    .commit();
        }
    }

}

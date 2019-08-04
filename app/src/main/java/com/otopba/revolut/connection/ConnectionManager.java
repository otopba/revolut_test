package com.otopba.revolut.connection;

import androidx.annotation.NonNull;

import io.reactivex.subjects.Subject;

public interface ConnectionManager {

    @NonNull
    Subject<Boolean> getConnectionSubject();

}

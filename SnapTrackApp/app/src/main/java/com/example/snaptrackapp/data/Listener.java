package com.example.snaptrackapp.data;

import com.google.firebase.database.annotations.Nullable;

public interface Listener<T> {
    void update(@Nullable T t);
}

package com.czjk.blelib.callback;


import com.czjk.blelib.exception.BleException;

public interface IBleCallback<T> {
    void onSuccess(T t, int type);
    void onFailure(BleException exception);
}

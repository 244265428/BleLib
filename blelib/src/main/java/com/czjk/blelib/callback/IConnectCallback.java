package com.czjk.blelib.callback;

import android.bluetooth.BluetoothGatt;

import com.czjk.blelib.exception.BleException;

public interface IConnectCallback {
    void onConnectSuccess(BluetoothGatt gatt, int status);
    void onConnectFailure(BleException exception);
    void onDisconnect();
}

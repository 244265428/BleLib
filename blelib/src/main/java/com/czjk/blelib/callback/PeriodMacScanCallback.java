package com.czjk.blelib.callback;

import android.bluetooth.BluetoothDevice;

import com.czjk.blelib.model.BleDevice;
import com.czjk.blelib.model.State;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class PeriodMacScanCallback extends PeriodScanCallback {
    private String mac;
    private AtomicBoolean hasFound = new AtomicBoolean(false);

    public PeriodMacScanCallback(String mac) {
        super();
        this.mac = mac;
        if (mac == null) {
            throw new IllegalArgumentException("start scan, mac can not be null!");
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (!hasFound.get()) {
            if (device != null && device.getAddress() != null && mac.equalsIgnoreCase(device.getAddress().trim())) {
                hasFound.set(true);
                if (viseBluetooth != null) {
                    viseBluetooth.stopLeScan(PeriodMacScanCallback.this);
                    viseBluetooth.setState(State.SCAN_SUCCESS);
                }
                onFind(new BleDevice(device, rssi, scanRecord));
            }
        }
    }
}

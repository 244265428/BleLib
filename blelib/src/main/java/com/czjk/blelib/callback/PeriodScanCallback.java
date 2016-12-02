package com.czjk.blelib.callback;

import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;

import com.czjk.blelib.ViseBluetooth;
import com.czjk.blelib.model.BleDevice;

public abstract class PeriodScanCallback implements LeScanCallback {

    protected Handler handler = new Handler(Looper.getMainLooper());
    protected ViseBluetooth viseBluetooth;
    protected int scanTimeout = -1; //-1表示一直扫描
    protected boolean isScan = true;
    protected boolean isScanning = false;


    public PeriodScanCallback setViseBluetooth(ViseBluetooth viseBluetooth) {
        this.viseBluetooth = viseBluetooth;
        return this;
    }

    public PeriodScanCallback setScanTimeout(int scanTimeout) {
        this.scanTimeout = scanTimeout;
        return this;
    }

    public PeriodScanCallback setScan(boolean scan) {
        isScan = scan;
        return this;
    }


    public void scan(){
        if(isScan){
            if(isScanning){
                return;
            }
            if(scanTimeout > 0){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isScanning = false;
                        if (viseBluetooth != null) {
                            viseBluetooth.stopLeScan(PeriodScanCallback.this);
                        }
                        scanTimeout();
                    }
                }, scanTimeout);
            }
            isScanning = true;
            if (viseBluetooth != null) {
                viseBluetooth.startLeScan(PeriodScanCallback.this);
            }
        } else{
            isScanning = false;
            if (viseBluetooth != null) {
                viseBluetooth.stopLeScan(PeriodScanCallback.this);
            }
        }
    }

    public PeriodScanCallback removeHandlerMsg(){
        handler.removeCallbacksAndMessages(null);
        return this;
    }

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
        onFind(new BleDevice(bluetoothDevice, rssi, scanRecord));
    }

    public abstract void scanTimeout();

    public abstract void onFind(BleDevice bleDevice);
}

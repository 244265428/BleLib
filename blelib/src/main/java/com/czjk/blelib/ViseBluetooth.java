package com.czjk.blelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.czjk.blelib.callback.IBleCallback;
import com.czjk.blelib.callback.IConnectCallback;
import com.czjk.blelib.callback.PeriodMacScanCallback;
import com.czjk.blelib.callback.PeriodScanCallback;
import com.czjk.blelib.exception.ConnectException;
import com.czjk.blelib.exception.GattException;
import com.czjk.blelib.exception.InitiatedException;
import com.czjk.blelib.exception.OtherException;
import com.czjk.blelib.exception.TimeoutException;
import com.czjk.blelib.model.BleDevice;
import com.czjk.blelib.model.State;
import com.czjk.blelib.utils.BleLog;
import com.czjk.blelib.utils.HexUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;


public class ViseBluetooth {

    public static final int DEFAULT_SCAN_TIME = 20000;
    public static final int DEFAULT_CONN_TIME = 10000;
    public static final int DEFAULT_OPERATE_TIME = 5000;
    private static final int MSG_WRITE_CHA = 1;
    private static final int MSG_READ_RSSI = 5;
    private static final int MSG_CONNECT_TIMEOUT = 6;
    public Context context;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattService service;
    private BluetoothGattCharacteristic characteristic;
    private IConnectCallback connectCallback;
    private IBleCallback tempBleCallback;
    private volatile Set<IBleCallback> bleCallbacks = new LinkedHashSet<>();
    private State state = State.DISCONNECT;
    private int scanTimeout = DEFAULT_SCAN_TIME;
    private int connectTimeout = DEFAULT_CONN_TIME;
    private int operateTimeout = DEFAULT_OPERATE_TIME;

    private static ViseBluetooth viseBluetooth;

    public static ViseBluetooth getInstance() {
        if (viseBluetooth == null) {
            synchronized (ViseBluetooth.class) {
                if (viseBluetooth == null) {
                    viseBluetooth = new ViseBluetooth();
                }
            }
        }
        return viseBluetooth;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CONNECT_TIMEOUT) {
                IConnectCallback connectCallback = (IConnectCallback) msg.obj;
                if (connectCallback != null && state != State.CONNECT_SUCCESS) {
                    close();
                    connectCallback.onConnectFailure(new TimeoutException());
                }
            } else {
                IBleCallback bleCallback = (IBleCallback) msg.obj;
                if (bleCallback != null) {
                    bleCallback.onFailure(new TimeoutException());
                    removeBleCallback(bleCallback);
                }
            }
            msg.obj = null;
        }
    };

    private BluetoothGattCallback coreGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            BleLog.i("onConnectionStateChange  status: " + status + " ,newState: " + newState +
                    "  ,thread: " + Thread.currentThread().getId());
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                gatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                state = State.DISCONNECT;
                BleLog.e(state.toString());
                if (handler != null) {
                    handler.removeMessages(MSG_CONNECT_TIMEOUT);
                }
                if (connectCallback != null) {
                    close();
                    runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status == 0) {
                                connectCallback.onDisconnect();
                            } else {
                                connectCallback.onConnectFailure(new ConnectException(gatt, status));
                            }
                        }
                    });
                }
            } else if (newState == BluetoothGatt.STATE_CONNECTING) {
                state = State.CONNECT_PROCESS;
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            BleLog.i("onServicesDiscovered  status: " + status);
            if (handler != null) {
                handler.removeMessages(MSG_CONNECT_TIMEOUT);
            }
            if (status == 0) {
                bluetoothGatt = gatt;
                state = State.CONNECT_SUCCESS;
                BleLog.e(state.toString());
                if (connectCallback != null) {
                    runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            connectCallback.onConnectSuccess(gatt, status);
                        }
                    });
                }
            } else {
                state = State.CONNECT_FAILURE;
                BleLog.e(state.toString());
                if (connectCallback != null) {
                    close();
                    runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            connectCallback.onConnectFailure(new ConnectException(gatt, status));
                        }
                    });
                }
            }
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
            BleLog.i("onCharacteristicWrite  status: " + status + ", data:" + HexUtil.encodeHexStr(characteristic.getValue()));
            if (bleCallbacks == null) {
                return;
            }
            if (handler != null) {
                handler.removeMessages(MSG_WRITE_CHA);
            }
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    for (IBleCallback<BluetoothGattCharacteristic> bleCallback : bleCallbacks) {
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            bleCallback.onSuccess(characteristic, 0);
                        } else {
                            bleCallback.onFailure(new GattException(status));
                        }
                    }
                    removeBleCallback(tempBleCallback);
                }
            });
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, final int rssi, final int status) {
            BleLog.i("onReadRemoteRssi  status: " + status + ", rssi:" + rssi);
            if (bleCallbacks == null) {
                return;
            }
            if (handler != null) {
                handler.removeMessages(MSG_READ_RSSI);
            }
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    for (IBleCallback<Integer> bleCallback : bleCallbacks) {
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            bleCallback.onSuccess(rssi, 0);
                        } else {
                            bleCallback.onFailure(new GattException(status));
                        }
                    }
                    removeBleCallback(tempBleCallback);
                }
            });
        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            String hexStr = HexUtil.encodeHexStr(characteristic.getValue());
            sb.append(hexStr);
            if (false == subPackageOnce()) {
                BleLog.e(sb.toString());

                EventBus.getDefault().post(sb.toString());
                sb.delete(0, sb.length());
            }
        }
    };
    public static StringBuffer sb = new StringBuffer();

    public static boolean subPackageOnce() {
        if (sb.toString().startsWith("2470")) {
            if (sb.length() >= 230) {
                return false;
            }
            return true;
        }
        return false;
    }


    private ViseBluetooth() {
    }

    public void init(Context context) {
        if (this.context == null) {
            this.context = context.getApplicationContext();
            bluetoothManager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    public void startLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.startLeScan(leScanCallback);
            state = State.SCAN_PROCESS;
        }
    }

    public void stopLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    public void startScan(PeriodScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setViseBluetooth(this).setScan(true).setScanTimeout(scanTimeout).scan();
    }

    public void stopScan(PeriodScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setViseBluetooth(this).setScan(false).removeHandlerMsg().scan();
    }

    public synchronized BluetoothGatt connect(BluetoothDevice bluetoothDevice, boolean autoConnect, IConnectCallback connectCallback) {
        if (bluetoothDevice == null || connectCallback == null) {
            throw new IllegalArgumentException("this BluetoothDevice or IConnectCallback is Null!");
        }
        if (handler != null) {
            Message msg = handler.obtainMessage(MSG_CONNECT_TIMEOUT, connectCallback);
            handler.sendMessageDelayed(msg, connectTimeout);
        }
        this.connectCallback = connectCallback;
        state = State.CONNECT_PROCESS;
        return bluetoothDevice.connectGatt(this.context, autoConnect, coreGattCallback);
    }

    public void connect(BleDevice bleDevice, boolean autoConnect, IConnectCallback connectCallback) {
        if (bleDevice == null) {
            throw new IllegalArgumentException("this BluetoothLeDevice is Null!");
        }
        connect(bleDevice.getDevice(), autoConnect, connectCallback);
    }

    public void connectByMac(String mac, final boolean autoConnect, final IConnectCallback connectCallback) {
        if (mac == null || mac.split(":").length != 6) {
            throw new IllegalArgumentException("Illegal MAC!");
        }
        startScan(new PeriodMacScanCallback(mac) {
            @Override
            public void onFind(final BleDevice bleDevice) {
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        connect(bleDevice, autoConnect, connectCallback);
                    }
                });
            }

            @Override
            public void scanTimeout() {
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (connectCallback != null) {
                            connectCallback.onConnectFailure(new TimeoutException());
                        }
                    }
                });
            }
        });
    }

    public ViseBluetooth withUUID(UUID serviceUUID, UUID characteristicUUID) {
        if (serviceUUID != null && bluetoothGatt != null) {
            service = bluetoothGatt.getService(serviceUUID);
        }
        if (service != null && characteristicUUID != null) {
            characteristic = service.getCharacteristic(characteristicUUID);
        }
        return this;
    }

    public boolean writeCharacteristic(byte[] data, IBleCallback<BluetoothGattCharacteristic> bleCallback) {
        return writeCharacteristic(getCharacteristic(), data, bleCallback);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data,
                                       final IBleCallback<BluetoothGattCharacteristic> bleCallback) {
        if (characteristic == null) {
            if (bleCallback != null) {
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        bleCallback.onFailure(new OtherException("this characteristic is null!"));
                        removeBleCallback(bleCallback);
                    }
                });
            }
            return false;
        }
        listenAndTimer(bleCallback, MSG_WRITE_CHA);
        characteristic.setValue(data);
        return handleAfterInitialed(getBluetoothGatt().writeCharacteristic(characteristic), bleCallback);
    }


    public boolean readRemoteRssi(IBleCallback<Integer> bleCallback) {
        listenAndTimer(bleCallback, MSG_READ_RSSI);
        return handleAfterInitialed(getBluetoothGatt().readRemoteRssi(), bleCallback);
    }

    public boolean enableCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                                    final IBleCallback<BluetoothGattCharacteristic> bleCallback,
                                                    boolean isIndication) {
        if (characteristic != null && (characteristic.getProperties() | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            return setCharacteristicNotification(getBluetoothGatt(), characteristic, true, isIndication);
        } else {
            if (bleCallback != null) {
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        bleCallback.onFailure(new OtherException("Characteristic [not supports] readable!"));
                        removeBleCallback(bleCallback);
                    }
                });
            }
            return false;
        }
    }

    public boolean setCharacteristicNotification(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 boolean enable,
                                                 boolean isIndication) {
        if (gatt != null && characteristic != null) {
            boolean success = gatt.setCharacteristicNotification(characteristic, enable);
            if (enable) {
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                if (descriptor != null) {
                    if (isIndication) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    } else {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    }
                    gatt.writeDescriptor(descriptor);
                    BleLog.i("Characteristic set notification is Success!");
                }
            }
            return success;
        }
        return false;
    }


    private boolean handleAfterInitialed(boolean initiated, final IBleCallback bleCallback) {
        if (bleCallback != null) {
            if (!initiated) {
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        bleCallback.onFailure(new InitiatedException());
                        removeBleCallback(bleCallback);
                    }
                });
            }
        }
        return initiated;
    }

    private synchronized void listenAndTimer(final IBleCallback bleCallback, int what) {
        if (bleCallbacks != null && handler != null) {
            this.tempBleCallback = bleCallback;
            bleCallbacks.add(bleCallback);
            Message msg = handler.obtainMessage(what, bleCallback);
            handler.sendMessageDelayed(msg, operateTimeout);
        }
    }

    public boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void runOnMainThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            if (handler != null) {
                handler.post(runnable);
            }
        }
    }

    public boolean isConnected() {
        if (state == State.CONNECT_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void removeBleCallback(IBleCallback bleCallback) {
        if (bleCallbacks != null && bleCallbacks.size() > 0) {
            bleCallbacks.remove(bleCallback);
        }
    }

    public synchronized boolean refreshDeviceCache() {
        try {
            final Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null && bluetoothGatt != null) {
                final boolean success = (Boolean) refresh.invoke(getBluetoothGatt());
                BleLog.i("Refreshing result: " + success);
                return success;
            }
        } catch (Exception e) {
            BleLog.e("An exception occured while refreshing device", e);
        }
        return false;
    }

    public synchronized void disconnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    public synchronized void close() {
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
        }
    }

    public synchronized void clear() {
        disconnect();
        refreshDeviceCache();
        close();
        if (bleCallbacks != null) {
            bleCallbacks.clear();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }


    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }


    public BluetoothGattService getService() {
        return service;
    }

    public ViseBluetooth setService(BluetoothGattService service) {
        this.service = service;
        return this;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public ViseBluetooth setScanTimeout(int scanTimeout) {
        this.scanTimeout = scanTimeout;
        return this;
    }

    public State getState() {
        return state;
    }

    public ViseBluetooth setState(State state) {
        this.state = state;
        return this;
    }
}

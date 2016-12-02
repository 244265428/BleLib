package com.czjk.blelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import com.czjk.blelib.callback.IBleCallback;
import com.czjk.blelib.callback.IConnectCallback;
import com.czjk.blelib.callback.PeriodScanCallback;
import com.czjk.blelib.db.DBTools;
import com.czjk.blelib.exception.BleException;
import com.czjk.blelib.model.BaseEvent;
import com.czjk.blelib.utils.BleLog;
import com.czjk.blelib.utils.BleUtil;
import com.czjk.blelib.utils.Constant;
import com.czjk.blelib.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.UUID;

import static com.czjk.blelib.utils.HexUtil.algorismToHEXString;
import static com.czjk.blelib.utils.HexUtil.hexStringToAlgorism;


public class ProtocolUtils {


    public static final UUID SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_UUID_WRITE_BASIC = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_UUID_NOTIFY_BASIC = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static ProtocolUtils protocolUtils;
    private static ViseBluetooth viseBluetooth;
    private PeriodScanCallback periodScanCallback;
    private Context context;

    public static ProtocolUtils getInstance() {
        if (protocolUtils == null) {
            synchronized (ProtocolUtils.class) {
                if (protocolUtils == null) {
                    protocolUtils = new ProtocolUtils();
                }
            }
        }
        return protocolUtils;
    }

    public void init(Context context) {
        this.context = context;
        if (!BleUtil.isBleEnable(context)) {
            BleUtil.openBle(context);
        }
        viseBluetooth = ViseBluetooth.getInstance();
        viseBluetooth.init(context);
        SPUtil.getInstance(context);
        DBTools.getInstance(context);

    }

    private IBleCallback bleCallback = new IBleCallback() {
        @Override
        public void onSuccess(Object o, int type) {
        }

        @Override
        public void onFailure(BleException exception) {
        }
    };

    public void connect(String mac) {
        SPUtil.setStringValue(Constant.SP_KEY_DEVICE_ADDRESS, mac);
        viseBluetooth.connectByMac(mac, false, new IConnectCallback() {
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                BluetoothGattCharacteristic characteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_UUID_NOTIFY_BASIC);
                if (characteristic != null) {
                    boolean success = viseBluetooth.enableCharacteristicNotification(characteristic, bleCallback, false);
                    if (success) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STATE_CONNECTED));
                    }
                }
            }

            @Override
            public void onConnectFailure(BleException exception) {
                BleLog.e(exception.toString());
                EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STATE_CONNECT_FAILURE));
            }

            @Override
            public void onDisconnect() {
                clear();
                EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STATE_DISCONNECTED));
            }
        });
    }

    public void scanDevices(PeriodScanCallback periodScanCallback) {
        this.periodScanCallback = periodScanCallback;
        viseBluetooth.setScanTimeout(10000).startScan(periodScanCallback);
    }

    public void stopScan() {
        if (periodScanCallback == null) {
            return;
        }
        viseBluetooth.stopScan(periodScanCallback);
    }

    public void disconnect() {
        if (isConnDevice()) {
            viseBluetooth.disconnect();
        }
    }

    public void clear() {
        viseBluetooth.clear();
    }

    public boolean isConnDevice() {
        return viseBluetooth.isConnected();
    }

    public void setUnBind() {
        String mac = SPUtil.getStringValue(Constant.SP_KEY_DEVICE_ADDRESS, null);
        if (mac == null) {
            return;
        }
        sendCmd(Constant.CMD_ID_BIND,Constant.KEY_UNBIND);
    }

    public void setClock() {
        sendCmd(Constant.CMD_ID_GET,Constant.KEY_GET_TIME);
    }

    public void getFW(){
        sendCmd(Constant.CMD_ID_GET,Constant.KEY_GET_FW);
    }
    public void getEnerge(){
        sendCmd(Constant.CMD_ID_GET,Constant.KEY_GET_ELECTRICITY);
    }

    public  void setOTA(){
        sendCmd(Constant.CMD_ID_DFU,Constant.KEY_OTA);
    }

    private void sendCmd(byte cmdId,byte key){
        byte[] cmd = new byte[20];
        cmd[0] = cmdId;
        cmd[1] = key;
        for (int i = 2; i < 20; i++) {
            cmd[i] = Constant.RESERVED;
        }
        write(cmd);
    }

    public   void setTime(String data) {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int year = now.get(Calendar.YEAR);
        int todayWeek = now.get(Calendar.DAY_OF_WEEK) - 2;
        if (todayWeek == -1) {
            todayWeek = 6;
        }
        int y = hexStringToAlgorism(data.substring(6, 8) + data.substring(4, 6));
        int mo = hexStringToAlgorism(data.substring(8, 10));
        int d = hexStringToAlgorism(data.substring(10, 12));
        int h = hexStringToAlgorism(data.substring(12, 14));
        int mi = hexStringToAlgorism(data.substring(14, 16));
        if (year != y || month != mo || day != d || hour != h || minute != mi) {
            byte[] cmd = new byte[20];
            cmd[0] = Constant.CMD_ID_SET;
            cmd[1] = Constant.KEY_SET_TIME;
            String hex =  algorismToHEXString(year);
            int height = Integer.parseInt(hex.substring(0,2));
            int light = Integer.parseInt(hex.substring(2,4),16);
            cmd[2] = (byte)light;
            cmd[3] = (byte)height;
            cmd[4] = (byte) month;
            cmd[5] = (byte) day;
            cmd[6] = (byte) hour;
            cmd[7] = (byte) minute;
            cmd[8] = (byte) second;
            cmd[9] = (byte) todayWeek;
            for (int i = 10; i < 20; i++) {
                cmd[i] = 0x00;
            }
            write(cmd);
        }
    }

    private  void write(byte[] params) {
        viseBluetooth.withUUID(SERVICE_UUID, CHARACTERISTIC_UUID_WRITE_BASIC)
                .writeCharacteristic(params, new IBleCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic, int type) {

                    }

                    @Override
                    public void onFailure(BleException exception) {
                        BleLog.i("write", "onFailure: " + exception.getDescription());
                    }
                });
    }


}

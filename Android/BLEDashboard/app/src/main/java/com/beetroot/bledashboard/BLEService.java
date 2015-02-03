package com.beetroot.bledashboard;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Adrian on 2/2/2015.
 */
public class BLEService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new BLEServiceBinder();

    private BluetoothManager mBTManager;
    private BluetoothAdapter mBTAdapter;
    private BluetoothGatt mBTGatt;
    private String mBTAddress;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class BLEServiceBinder extends Binder {
        BLEService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BLEService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mBTGatt != null) {
            mBTGatt.close();
            mBTGatt = null;
        }
        return super.onUnbind(intent);
    }
}

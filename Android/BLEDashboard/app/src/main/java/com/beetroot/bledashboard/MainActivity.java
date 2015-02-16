package com.beetroot.bledashboard;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SEARCH_TIMEOUT = 10000; //10 seconds should be plenty

    private BluetoothAdapter mBluetoothAdapter;
    private static List<BluetoothDevice> mBleDevices = new ArrayList<BluetoothDevice>();
    private Context context;

    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

        InitBLE ();
        SearchForBLEDevices ();
    }

    private void InitBLE()
    {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) == false) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (mBluetoothAdapter.isEnabled() == false) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void SearchForBLEDevices ()
    {
        new Thread() {

            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mBleScanCallback);

                try {
                    Thread.sleep(SEARCH_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mBluetoothAdapter.stopLeScan(mBleScanCallback);
            }
        }.start();
    }

    private BluetoothAdapter.LeScanCallback mBleScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device != null) {
                        if (mBleDevices.indexOf(device) == -1) //only add new devices
                        {
                            mBleDevices.add(device);

                            ArrayList<String> listValues = new ArrayList<String>();

                            for (BluetoothDevice device : mBleDevices)
                            {
                                listValues.add(device.getName());
                            }

                            ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(context, R.layout.row_layout, R.id.listText, listValues);

                            setListAdapter(myAdapter);
                        }
                    }
                }



            });
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        Toast.makeText(this, "Clicked row " + position, Toast.LENGTH_SHORT).show();

        //navigate to dashboard
        Intent dashboardIntent = new Intent(getApplicationContext(),
                DashboardActivity.class);
        startActivity(dashboardIntent);

    }
}

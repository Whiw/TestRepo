

package com.young.android.bleproject;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends ListActivity {
    MeasureService mService;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private boolean mBound;
    private Handler mHandler;
    private Menu menu;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    public Pair<BluetoothDevice, BluetoothDevice> pair;
    public int pairnum1;
    public int pairnum2;
    public int paircount = 0;

    public static ArrayList<Pair<BluetoothDevice, BluetoothDevice>> comparablepair = new ArrayList<>();

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MeasureService.LocalBinder binder = (MeasureService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(R.string.title_devices);
        comparablepair.removeAll(comparablepair);
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
    }
        ViewGroup x = (ViewGroup)getListView().getParent();
        this.getLayoutInflater().inflate(R.layout.timevar, x);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        /*if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        */


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            */

            case R.id.menu_scan:
                item.setEnabled(false);

                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                /*
                lHandler.post(new Runnable() {

                @Override
                public void run() {

/*
                                mLeDeviceListAdapter.clear();
*/

                    /*

                    scanLeDevice(true);
                    mLeDeviceListAdapter.notifyDataSetChanged();


                    lHandler.postDelayed(this, 500);
                    */
                /*
                    if (isScan == true) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        isScan = false;
                    }
                    else {
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                        isScan = true;
                    }
                    lHandler.postDelayed(this, 500);

                }
            });
            */




                break;
            case R.id.menu_pair:
                boolean x = true;
                if (paircount != 2)
                    break;
                else {
                    pair = new Pair<BluetoothDevice, BluetoothDevice>();
                    if (mLeDeviceListAdapter.getDevice(pairnum1).getName().contains("UV") && mLeDeviceListAdapter.getDevice(pairnum2).getName().contains("TH")) {
                        pair.setFirst(mLeDeviceListAdapter.getDevice(pairnum1));
                        pair.setSecond(mLeDeviceListAdapter.getDevice(pairnum2));
                    }
                    else if (mLeDeviceListAdapter.getDevice(pairnum1).getName().contains("TH") && mLeDeviceListAdapter.getDevice(pairnum2).getName().contains("UV"))
                    {
                        pair.setFirst(mLeDeviceListAdapter.getDevice(pairnum2));
                        pair.setSecond(mLeDeviceListAdapter.getDevice(pairnum1));
                    }
                    else
                        break;
                    for(int i = 0; i < comparablepair.size(); i++) {
                        if (pair.getFirst().getAddress().compareTo(comparablepair.get(i).getFirst().getAddress()) == 0 || pair.getSecond().getAddress().compareTo(comparablepair.get(i).getSecond().getAddress()) == 0)
                        {
                            x = false;
                            break;
                        }

                    }
                    if (x) {

                        comparablepair.add(pair);
                        TextView pairnumtextview2 = (TextView) findViewById(R.id.Pairnum);
                        pairnumtextview2.setText("Pair number : " + String.valueOf(comparablepair.size()));
                        Toast.makeText(getApplicationContext(), "Pair added", Toast.LENGTH_SHORT).show();

                    }
                        getListView().getChildAt(pairnum1).setBackgroundColor(0x00000000);
                    getListView().getChildAt(pairnum1).setEnabled(true);
                    getListView().getChildAt(pairnum2).setBackgroundColor(0x00000000);
                    getListView().getChildAt(pairnum2).setEnabled(true);
                    paircount-=2;

                }

                break;
            case R.id.menu_graphntable:
                Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_reset:
                mHandler.removeMessages(0);
                menu.findItem(R.id.menu_scan).setEnabled(true);
                mLeDeviceListAdapter.clear();
                comparablepair.removeAll(comparablepair);
                scanLeDevice(false);
                mLeDeviceListAdapter.notifyDataSetChanged();
                Intent lintent = new Intent(this, MeasureService.class);
                stopService(lintent);
                makeZeroing();
                View k = findViewById(R.id.executeButton);
                k.setEnabled(true);
                TextView pairnumtextview3 = (TextView) findViewById(R.id.Pairnum);
                pairnumtextview3.setText("Pair number : " + String.valueOf(0));
                break;
        }
        return true;
    }
    public void makeZeroing() {
        for (int i =0; i < comparablepair.size(); i++) {
            for (int j = 0; j < MeasureService.timevaluearray.length; j++) {
                MeasureService.UVvaluearray[i][j] = 0.0;
                MeasureService.IRvaluearray[i][j] = 0.0;
                MeasureService.VISvaluearray[i][j] = 0.0;
                MeasureService.timevaluearray[j] = 0.0;
                MeasureService.THvaluearray1[i][j] = 0.0;
                MeasureService.THvaluearray2[i][j] = 0.0;
            }
        }
        MeasureService.count = 0;


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        /*
        scanLeDevice(true);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
        Intent intent = new Intent(this, MeasureService.class);
        stopService(intent);
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }
    @Override
    protected void onDestroy() {
        mHandler.removeMessages(0);
        scanLeDevice(false);
        makeZeroing();
        mLeDeviceListAdapter.clear();
        Intent intent = new Intent(this, MeasureService.class);
        stopService(intent);
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onDestroy();

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(!v.isEnabled()) {
            v.setBackgroundColor(0x00000000);
            v.setEnabled(true);
            paircount--;
            String x = String.valueOf(paircount);

        }
        else if (v.isEnabled()){
            v.setBackgroundColor(0xC0C0C0C0);
            paircount++;
            if (paircount == 1)
                pairnum1 = position;
            else if (paircount == 2)
                pairnum2 = position;
            v.setEnabled(false);

        }




        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;


    }
    public void execution(View v) {

        EditText x = (EditText) findViewById(R.id.TimeInterval);
        EditText y = (EditText) findViewById(R.id.TimeLimit);
        long timeinterval = Long.parseLong(x.getText().toString());
        long timelimit = Long.parseLong(y.getText().toString());

        Intent intent = new Intent(this, MeasureService.class);

        intent.putExtra("TimeInterval", timeinterval);
        intent.putExtra("Timelimit", timelimit);

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
        v.setEnabled(false);



    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {

            // Stops scanning after a pre-defined scan period.

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mScanning== false) {
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                        mScanning = true;
                    }
                    else {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mScanning = false;
                    }

                    mHandler.postDelayed(this, 1500);

                }
            });




        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                if (device.getName() != null && device.getName().length() > 0)
                    if (device.getName().contains("UV") || device.getName().contains("TH"))
                        mLeDevices.add(device);
            }

        }


        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            String Data[]= null;
            if (deviceName != null && deviceName.length() > 0) {
                Data = deviceName.split(":");
                viewHolder.deviceName.setText(Data[0]);
            }
            else {/*
                viewHolder.deviceName.setText(R.string.unknown_device);
                */

                mLeDevices.remove(device);
            }
            if (Data != null) {
                if (Data[0].equals("UVI")) {
                    String result = "UV : " + Data[1] + "\u00B5W/cm\u00B2\nVS : " + Data[2] + "mW/cm²\nIR : "+ Data[3] + "µW/cm²";
                    viewHolder.deviceAddress.setText(result);
                }
                else if (Data[0].equals("TH")) {
                    String result = "Temp1 : " + Data[1] + "\u00BAC\nTemp2 : " + Data[3] + "ºC";
                    viewHolder.deviceAddress.setText(result);
                }
            }
            else
                viewHolder.deviceAddress.setText(device.getName());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}
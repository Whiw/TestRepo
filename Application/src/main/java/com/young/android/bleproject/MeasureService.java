package com.young.android.bleproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by young on 2016-06-10.
 */
public class MeasureService extends Service {
    public ArrayList<Pair<BluetoothDevice, BluetoothDevice>> x = DeviceScanActivity.comparablepair;
    @Nullable
    private NotificationManager mNM;
    public long timeinterval;
    public long timelimit;
    public long starttime;


    public static double[][] UVvaluearray;
    public static double[][] VISvaluearray;
    public static double[][] IRvaluearray ;
    public static double[][] THvaluearray1;
    public static double[][] THvaluearray2;
    public static double[] timevaluearray;
    static int count = 0;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.

    private void putUVvalue(int pairnum, int i) {

        String Forparse;

        String data[];
        Forparse = DeviceScanActivity.comparablepair.get(pairnum).getFirst().getName();
        data = Forparse.split(":");
        UVvaluearray[pairnum][i] = Double.parseDouble(data[1]);

    }
    private void putVISvalue(int pairnum, int i) {

        String Forparse;

        String data[];
        Forparse = DeviceScanActivity.comparablepair.get(pairnum).getFirst().getName();
        data = Forparse.split(":");
        VISvaluearray[pairnum][i] = Double.parseDouble(data[2]);

    }
    private void putIRvalue(int pairnum, int i) {

        String Forparse;

        String data[];
        Forparse = DeviceScanActivity.comparablepair.get(pairnum).getFirst().getName();
        data = Forparse.split(":");
        IRvaluearray[pairnum][i] = Double.parseDouble(data[3]);

    }
    private void putTHvalue1(int pairnum, int i) {

        String Forparse;

        String data[];
        Forparse = DeviceScanActivity.comparablepair.get(pairnum).getSecond().getName();
        data = Forparse.split(":");
        THvaluearray1[pairnum][i] = Double.parseDouble(data[1]);

    }
    private void putTHvalue2(int pairnum, int i) {

        String Forparse;

        String data[];
        Forparse = DeviceScanActivity.comparablepair.get(pairnum).getSecond().getName();
        data = Forparse.split(":");
        THvaluearray2[pairnum][i] = Double.parseDouble(data[3]);

    }
    private void puttimevalue(int i, double time) {

        timevaluearray[i] = time;



    }

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        MeasureService getService() {
            return MeasureService.this;
        }
    }

    @Override
    public void onCreate() {

        // Display a notification about us starting.  We put an icon in the status bar.

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        final Handler ServiceHandler = new Handler();
        starttime = System.currentTimeMillis();

        timeinterval = intent.getExtras().getLong("TimeInterval");
        timelimit = intent.getExtras().getLong("Timelimit");
        long x = timelimit / timeinterval;
        int k = Integer.parseInt(Long.toString(x));
        timevaluearray = new double[k+1];
        UVvaluearray = new double[DeviceScanActivity.comparablepair.size()][k+1];
        VISvaluearray = new double[DeviceScanActivity.comparablepair.size()][k+1];
        THvaluearray1 = new double[DeviceScanActivity.comparablepair.size()][k+1];
        THvaluearray2 = new double[DeviceScanActivity.comparablepair.size()][k+1];
        IRvaluearray = new double[DeviceScanActivity.comparablepair.size()][k+1];
        ServiceHandler.post(new Runnable() {
            @Override
            public void run() {

                puttimevalue(count, Double.parseDouble(Long.toString(timeinterval * count)));
                for(int i =0; i < DeviceScanActivity.comparablepair.size(); i++) {
                    putUVvalue(i, count);
                    putIRvalue(i, count);
                    putVISvalue(i, count);
                    putTHvalue1(i, count);
                    putTHvalue2(i, count);
                }
                ServiceHandler.postDelayed(this, timeinterval);
                count++;
                measuredone(ServiceHandler);
            }
        });


        return START_STICKY;
    }

    public void measuredone(Handler x) {

        if (starttime + timelimit < System.currentTimeMillis())
        {

            Toast.makeText(getApplicationContext(), "measure process is done", Toast.LENGTH_SHORT).show();

            int k = count;
            x.removeMessages(0);
            count-=k;
            PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, ChartActivity.class), 0);
            Resources r = getResources();
            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(r.getString(R.string.app_name))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(r.getString(R.string.app_name))
                    .setContentText("Measure process is done")
                    .setContentIntent(pi)
                    .setAutoCancel(true).setVibrate(new long[] {500, 500, 500, 500})
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }
    }
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.


        // Tell the user we stopped.

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.app_name);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.tile, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DeviceScanActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        /*
        notification.setLatestEventInfo(this, getText(R.string.app_name),
                text, contentIntent);
        */
        // Send the notification.

    }

}

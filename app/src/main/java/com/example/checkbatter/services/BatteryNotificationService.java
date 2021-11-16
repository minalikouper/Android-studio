package com.example.checkbatter.services;


import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.checkbatter.MainActivity;
import com.example.checkbatter.R;
import com.example.checkbatter.model.Battery;

import java.util.Timer;
import java.util.TimerTask;

public class BatteryNotificationService extends Service {

    public static final int NOTIFICATION_ID = 1;
    public static final int notify = 30000;  //interval between two services(Here Service run every 5 Minute)
    private BroadcastReceiver mBroadcastReceiver;
    private ImageView batteryImg;
    private ImageView androidImg;
    private IBinder mBinder = new MyBackgroundServiceBinder();
    private Battery battery;
    private int level = 0;
    private ObservableMn observableMn;
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
        observableMn = new ObservableMn();
        observableMn.batteryObservable(BatteryNotificationService.this)
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(in -> {
                    Log.i("Battery", "from service " + in);

                    int colorBt = in.getColorBt();
                    int valueBt = in.getValueBt();
                    int surl = in.getUrl();
                    boolean isSouns = in.getSoundBt();
                    String messageBody = in.getMessage();
                    sendNotification(messageBody, surl, colorBt);

                }, throwable -> Log.e(TAG, ("Throwable " + throwable)));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Log.d("service is ", "Destroyed");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String messageBody, int surl, int color) {
        Intent intent = new Intent(this, MainActivity.class);//**The activity that you want to open when the notification is clicked
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + surl);


        @SuppressLint("WrongConstant") Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_battery_low)
                .setContentTitle("уведомление")
                .setContentText(messageBody)
                .setColor(getColor(color))
                .setPriority(5)
                .setOngoing(true)
                .setSound(sound);
//                .setOnlyAlertOnce(true);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        startForeground(NOTIFICATION_ID, notificationBuilder.build());

    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    Log.d("service is ", "running");

                }
            });
        }
    }

    public class MyBackgroundServiceBinder extends Binder {
        public BatteryNotificationService getService() {

            return BatteryNotificationService.this;
        }
    }

}

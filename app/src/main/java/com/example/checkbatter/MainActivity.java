package com.example.checkbatter;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.checkbatter.model.Battery;
import com.example.checkbatter.services.BatteryNotificationService;
import com.example.checkbatter.services.ObservableMn;

import org.reactivestreams.Subscription;

import reciverce.BatteryReciver;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Switch switchSilent;
    private SeekBar volumeSeekBar;
    private ProgressBar progressBar;
    private ImageView androidImg;
    private ImageButton btnExit;
    private int level = 0;
    private TextView textBtValue;
    private TextView progressText;
    private TextView health;
    private TextView temp;
    private Battery battery;
    private boolean isBound = false;
    private BatteryReciver batteryReciver;
    private NotificationManager notificationManager;
    private Subscription subscription;
    private ObservableMn observableMn;
    private BatteryNotificationService batteryNotificationService;
    private boolean isBn = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchSilent = findViewById(R.id.switchSilent);
        btnExit = findViewById(R.id.btnExit);

        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.text_view_progress);
        androidImg = findViewById(R.id.androidImg);

        temp = (TextView) findViewById(R.id.temp_value);
        health = (TextView) findViewById(R.id.heals_value);

        observableMn = new ObservableMn();
        observableMn.batteryObservable(MainActivity.this)
                .subscribe(in -> {
                            Log.i("Battery", "from main " + in.getHealth());
                            int cl = in.getColorBt();
                            int val = in.getValueBt();
                            String hl = in.getHealthString();
                            int tm = in.getTemp();
                            boolean isSound = in.getSoundBt();
                            temp.setText(tm + " °C");
                            health.setText(hl);

                            androidImg.setColorFilter(ContextCompat.getColor(MainActivity.this, cl), PorterDuff.Mode.SRC_IN);
                            progressText.setTextColor(getResources().getColor(cl, null));
                            progressText.setText(val + "%");

                            Drawable batteryProgressD = progressBar.getProgressDrawable();
                            batteryProgressD.setLevel(val * 100);
                            progressBar.setProgress(val);

//-----------progress bar check version
//                            if (progressBar != null) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    RotateDrawable rotateDrawable = (RotateDrawable) progressBar.getIndeterminateDrawable();
//                                    rotateDrawable.setToDegrees(0);
//                                }
//                            }

//                    batteryImg.setColorFilter(ContextCompat.getColor(MainActivity.this, cl), PorterDuff.Mode.SRC_IN);


                        },
                        throwable -> Log.e(TAG, ("Throwable " + throwable))
                );

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                BatteryNotificationService.MyBackgroundServiceBinder binder = (BatteryNotificationService.MyBackgroundServiceBinder) service;
                batteryNotificationService = binder.getService();
                isBound = true;
            }


            @Override
            public void onServiceDisconnected(ComponentName name) {
                batteryNotificationService = null;
                isBn = false;
            }
        };
//--------- start foreground service
        Intent serviceIntent = new Intent(this, BatteryNotificationService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startForegroundService(serviceIntent);
        } else {
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }

//----- button exit
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
            }
        });


    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        observableMn.batteryObservable(MainActivity.this).unsubscribeOn(Schedulers.io());


    }
}




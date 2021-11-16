package com.example.checkbatter.services;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.example.checkbatter.model.Battery;
import com.github.karczews.rxbroadcastreceiver.RxBroadcastReceivers;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class ObservableMn {


    public Observable<Battery> batteryObservable(final Context context) {

        Observable<Battery> btObservable;
        btObservable = RxBroadcastReceivers.fromIntentFilter((context), new IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                .doOnError(throwable -> Log.e(TAG, "Throwable " + throwable.getMessage()))
                .map(intent -> {
//                    intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    int lv = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int hl = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
                    int tm = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                    Battery bt = new Battery(lv, hl, tm);
                    return bt;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e(TAG, "Throwable " + throwable.getMessage()));

//        btObservable = Observable.zip(level.subscribeOn(Schedulers.newThread()),
//                                        health.subscribeOn(Schedulers.newThread()),
//                                        temp.subscribeOn(Schedulers.newThread()), Battery::new);


        return btObservable;

    }


}

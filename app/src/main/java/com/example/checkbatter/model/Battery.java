package com.example.checkbatter.model;

import android.util.Log;

import com.example.checkbatter.R;

import java.io.Serializable;


public class Battery implements Serializable {

    int valueBt;
    int colorBt;
    String message;
    int url;
    int health;
    String healthString;
    int temp;
    boolean soundBt = true;

    public Battery(int valueBt, int health, int temp) {
        this.valueBt = valueBt;
        this.setStartData();
        this.health = health;
        this.temp = temp;
        this.setHealthString();
    }

    private void setStartData() {

        if (this.valueBt >= 0 && this.valueBt <= 30) {
            this.colorBt = R.color.RED;
            this.url = R.raw.voicelowbattery;
            this.message = "подключите ,пожалуйста, зарядное устройство!";
        } else if (this.valueBt == 100) {
            this.colorBt = R.color.GREEN;
            this.url = R.raw.voicechargetbattery;
            this.message = "отключите ,пожалуйста, зарядное устройство!";
        } else if (this.valueBt > 30 && this.valueBt < 70) {
            this.colorBt = R.color.YELLOW;
            this.url = 0;
            this.message = "заряд батареи : " + this.valueBt;
        } else {
            this.colorBt = R.color.GREEN;
            this.url = R.raw.strtsignal;
            this.message = "заряд батареи : " + this.valueBt;

        }
    }

    public int getUrl() {
        return this.url;
    }

    public String getMessage() {
        return this.message;
    }

    public int getHealth() {
        return health;
    }

    public int getTemp() {
        return temp;
    }

    private void setHealthString() {
        Log.i("Battery", "health " + this.health);
        if (this.health == 2) {
            this.healthString = "Хорошее";

        } else if (this.health == 3) {
            this.healthString = "Перегрев";
        } else if (this.health == 4) {
            this.healthString = "Плохое";
        } else if (this.health == 5) {
            this.healthString = "Перенапряжение";
        } else if (this.health == 6) {
            this.healthString = "Unspecified Failure";
        } else if (this.health == 7) {
            this.healthString = "холодное";
        } else
            this.healthString = "неизвестное";
    }

    public String getHealthString() {
        return this.healthString;
    }

    public int getValueBt() {
        return this.valueBt;
    }

    public void setValueBt(int valueBt) {
        this.valueBt = valueBt;
    }

    public boolean getSoundBt() {
        return this.soundBt;
    }

    public void setSoundBt(boolean soundBt) {
        this.soundBt = soundBt;
    }

    public int getColorBt() {
        return this.colorBt;
    }


}

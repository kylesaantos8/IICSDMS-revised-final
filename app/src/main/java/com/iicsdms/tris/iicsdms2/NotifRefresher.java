package com.iicsdms.tris.iicsdms2;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kyle on 26/04/2018.
 */

public class NotifRefresher {

    Timer timer;
    TimerTask timerTask;
    int secInterval = 5;

    SharedPreferences sharedPreferences;

    final Handler handler = new Handler();

    public void startTimer()
    {
        timer = new Timer();
        initializeTimerTask();

        timer.schedule(timerTask, secInterval*1000, secInterval*1000);
    }

    public void stopTimer()
    {
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask()
    {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        };
    }
}

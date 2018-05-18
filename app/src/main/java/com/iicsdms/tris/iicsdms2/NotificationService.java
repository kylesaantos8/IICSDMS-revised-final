package com.iicsdms.tris.iicsdms2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kyle on 18/04/2018.
 */

public class NotificationService extends Service{

    Timer timer;
    TimerTask timerTask;
    int secInterval = 5;

    static final String TAG = "test";

    RetrieveData retrieveData;
    SharedPreferences sharedPreferences;
    List<Map<String,String>> file = new ArrayList<Map<String, String>>();
    String [] notifContent;
    String notifLink, logEmail, notifDesc;
    int notif, countNotif, notifDiff;
    int notifDef;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        startTimer();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        stopTimer();
        super.onDestroy();
    }

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

                        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

                        notifLink = getApplicationContext().getString(R.string.notif_service_url);
                        logEmail = sharedPreferences.getString("logEmail", TAG);
                        countNotif = sharedPreferences.getInt("notifCount", notifDef);

                        retrieveData = new RetrieveData();
                        file = retrieveData.getNotif(notifLink, logEmail);
                        notif = file.size();

                        if(countNotif != notif)
                        {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("notifCount", notif);


                            notifDiff = notif - countNotif;
                            if(notifDiff > 0)
                            {
                                for(int i = 0; i< notif; i++)
                                {
//                                    notifContent = new String[]{};
//                                    notifContent[i] = file.get(i).get("notifDescription");
                                    notifDesc = file.get(i).get("notifDescription");
//                                    Log.e(TAG, notifDesc);
                                }

                                editor.putInt("notifCounter", notifDiff);
                                notif(notifDiff, notifContent);
                            }
                            editor.apply();
                        }
                        else
                        {
//                            Log.e(TAG, String.valueOf(countNotif));
                        }
                    }
                });
            }
        };
    }

    public void notif(int notif, String [] notifContent)
    {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.icon_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.icon_launcher))
                .setSound(uri)
                .setVibrate(new long[] {500, 500, 500})
                .setLights(Color.YELLOW, 3000, 3000)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("You have " + notif + " new notification!");

        Intent resultIntent = new Intent(getApplicationContext(), PreLogActivity.class);
        resultIntent.putExtra("NotifTab", "notify");
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        notifBuilder.setContentIntent(resultPendingIntent);
        notifBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifBuilder.build());
    }
}

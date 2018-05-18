package com.iicsdms.tris.iicsdms2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import com.github.sundeepk.compactcalendarview.CompactCalendarView;
//import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class CalendarFragment extends Fragment{
    private static final String TAG = "Test";

    List<Map<String,String>> file = new ArrayList<Map<String, String>>();

    Button btnNotif;
    String link, logEmail;

    RetrieveData retrieveData;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        link = getString(R.string.cal_inv_url);
        link = getString(R.string.notif_url);
//        logEmail = getActivity().getIntent().getStringExtra("logEmail");
        logEmail = sharedPreferences.getString("logEmail",TAG);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        retrieveData = new RetrieveData(getContext());


        btnNotif = (Button) rootView.findViewById(R.id.btn_notif);


        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notif();
            }
        });


        return rootView;
    }

    public void startService()
    {

    }

    public void notif()
    {
        NotificationCompat.Builder notifBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.icon_launcher)
                .setVibrate(new long[] {500, 500, 500})
                .setLights(Color.YELLOW, 3000, 3000)
                .setContentTitle("Test Notification")
                .setContentText("Hello!");

        Intent resultIntent = new Intent(getContext(), MainActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getContext());
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        notifBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifBuilder.build());
    }


}

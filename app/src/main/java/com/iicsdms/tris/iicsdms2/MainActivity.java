package com.iicsdms.tris.iicsdms2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    SharedPreferences sharedPreferences;

static final String TEST = "test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction =  fragmentManager.beginTransaction();

//                String notifTab = getIntent().getStringExtra("NotifTab");
                sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("logEmail", getIntent().getStringExtra("logEmail"));
                editor.putString("logPw", getIntent().getStringExtra("logPw"));
                editor.apply();


//                if(notifTab == null)
//                {

                    switch (item.getItemId()) {
                        case R.id.navigation_inbox:
                            transaction.replace(R.id.content, new NotificationFragment()).commit();
                            return true;
//                    case R.id.navigation_calendar:
//                        transaction.replace(R.id.content, new CalendarAndInviteFragment()).commit();
//                        return true;
                        case R.id.navigation_documents:
                            transaction.replace(R.id.content, new DocumentsFragment()).commit();
                            return true;
                        case R.id.navigation_profile:
                            transaction.replace(R.id.content, new ProfileFragment()).commit();
                            return true;
                    }
//                }
//                else if (notifTab.equals("notify"))
//                {
//                    transaction.replace(R.id.content, new NotificationFragment()).commit();
//                    return true;
//                }
                return false;
            }

        };

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_inbox);
        //BottomNavigationViewHelper.disableShiftMode(navigation);
//
//        BottomNavigationMenuView bottomNavigationMenuView =
//                (BottomNavigationMenuView) navigation.getChildAt(0);
//        View v = bottomNavigationMenuView.getChildAt(3);
//        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
//
//        View badge = LayoutInflater.from(this)
//                .inflate(R.layout.notification_badge, bottomNavigationMenuView, false);
//
//        itemView.addView(badge);



    }

}

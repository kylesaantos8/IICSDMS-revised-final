package com.iicsdms.tris.iicsdms2;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;



public class NotificationFragment extends Fragment implements MyNotifRecyclerViewAdapter.ClickListener, AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "Test";

    private RecyclerView recyclerView;

    List<Map<String,String>> file = new ArrayList<Map<String, String>>();
    ArrayList<NotifPojo> inbox = new ArrayList<>();

    String logEmail, link, linkSeen, linkSeenUpdate;
    int notifCounter;
    int notifStart = 0;

    Toolbar toolbar;

    View view;

    SharedPreferences sharedPreferences;

    TextView emptyView;

    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){

//        if(view != null)
//        {
//            if(view.getParent() != null)
//                ((ViewGroup) view.getParent()).removeView(view);
//            return view;
//
//        }


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        logEmail = sharedPreferences.getString("logEmail", TAG);
        view = inflater.inflate(R.layout.fragment_notif, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Notifications");

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        emptyView = (TextView) view.findViewById(R.id.empty_view_notif);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                swipeRefreshLayout.setRefreshing(true);
//                inbox.clear();
//                if(!file.isEmpty())
//                {
//                    for(int i = 0; i < file.size(); i++){
//                        Map<String,String> fileMap = file.get(i);
//                        NotifPojo f = new NotifPojo(
//                                fileMap.get("notifId"),
//                                fileMap.get("notifPage"),
//                                fileMap.get("notifDescription"),
//                                fileMap.get("notifTimeStamp"),
//                                fileMap.get("notifFlag"));
//                        inbox.add(f);
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }else
//                {
////                Toast.makeText(getContext(), "No notification available.", Toast.LENGTH_LONG).show();
//                    recyclerView.setVisibility(View.GONE);
//                    emptyView.setVisibility(View.VISIBLE);
//                    swipeRefreshLayout.setRefreshing(false);
////                Log.e(TAG, "empty");
//                }
//
//            }
//        });




        link = getString(R.string.notif_url);
        linkSeen = getString(R.string.seen_url);
        linkSeenUpdate = getString(R.string.seen_update_url);

        if(!logEmail.equals(TAG))
        {

            notifCounter = sharedPreferences.getInt("notifCounter", notifStart);
//
//            NotifRefresher notifRefresher = new NotifRefresher();
//            notifRefresher.startTimer();
//
//            if(notifCounter > notifStart)
//            {
//
//            }
//            else
//            {
//                Log.e(TAG, "stop...");
//            }

            RetrieveData retrieveData = new RetrieveData(getContext());
            file = retrieveData.getNotif(link,logEmail);

            if(!file.isEmpty())
            {
                for(int i = 0; i < file.size(); i++){
                    Map<String,String> fileMap = file.get(i);
                    NotifPojo f = new NotifPojo(
                            fileMap.get("notifId"),
                            fileMap.get("notifPage"),
                            fileMap.get("notifDescription"),
                            fileMap.get("notifTimeStamp"),
                            fileMap.get("notifFlag"));
                    inbox.add(f);

                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                MyNotifRecyclerViewAdapter listAdapter = new MyNotifRecyclerViewAdapter(getFragmentManager(), inbox,
                        getContext(), linkSeenUpdate, logEmail, this);
                DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(itemDecoration);
                recyclerView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();

                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            else
            {
//                Toast.makeText(getContext(), "No notification available.", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

//                Log.e(TAG, "empty");
            }

        }
        else
        {
//            palagyan ng dialog box dito.
//            na nakalagay "please log in to continue" tas tsaka magreredirect sa login page natin.


            Intent intent = new Intent(getContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            Toast.makeText(getContext(), "Please log in to continue", Toast.LENGTH_SHORT).show();

        }

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    @Override
//    public void onRefresh() {
////        getActivity().getSupportFragmentManager().beginTransaction()
////                .replace(R.id.content, new NotificationFragment())
////                .commit();
////        swipeRefreshLayout.setRefreshing(false);
//        inbox.clear();
//        if(!file.isEmpty())
//        {
//            for(int i = 0; i < file.size(); i++){
//                Map<String,String> fileMap = file.get(i);
//                NotifPojo f = new NotifPojo(
//                        fileMap.get("notifId"),
//                        fileMap.get("notifPage"),
//                        fileMap.get("notifDescription"),
//                        fileMap.get("notifTimeStamp"),
//                        fileMap.get("notifFlag"));
//                inbox.add(f);
//
//            }
//        }

//    @Override
//    public void onRefresh() {
//
//        refreshItems();
//    }
//
//    void refreshItems(){
//
//
//        onItemsLoadComplete();
//
//    }
//
//    void onItemsLoadComplete(){
//
//        swipeRefreshLayout.setRefreshing(false);
//
//    }

//    }

    @Override
    public void onItemClicked(int position) {

        getActivity().getSupportFragmentManager().beginTransaction()
        .replace(R.id.content, new NotificationFragment())
        .commit();

    }

    @Override
        public void onRefresh() {

            swipeRefreshLayout.setRefreshing(true);
            inbox.clear();
            if(!file.isEmpty())
            {
                for(int i = 0; i < file.size(); i++){
                    Map<String,String> fileMap = file.get(i);
                    NotifPojo f = new NotifPojo(
                            fileMap.get("notifId"),
                            fileMap.get("notifPage"),
                            fileMap.get("notifDescription"),
                            fileMap.get("notifTimeStamp"),
                            fileMap.get("notifFlag"));
                    inbox.add(f);
                    swipeRefreshLayout.setRefreshing(false);

                }

                MyNotifRecyclerViewAdapter listAdapter = new MyNotifRecyclerViewAdapter(getFragmentManager(), inbox,
                        getContext(), linkSeenUpdate, logEmail, this);
                recyclerView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();

            }else
            {
//                Toast.makeText(getContext(), "No notification available.", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
//                Log.e(TAG, "empty");
            }

            getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new NotificationFragment())
                .commit();


        }
}

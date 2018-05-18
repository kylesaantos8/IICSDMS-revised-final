package com.iicsdms.tris.iicsdms2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewThread extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    static final String TAG = "test";
    ListView listView;

    List<Map<String,String>> file = new ArrayList<Map<String, String>>();
    ArrayList<FilePojo> filePojoArrayList = new ArrayList<>();
    String link, logEmail;
    String id, thread, sourceRecipient, notifId;
    RetrieveData retrieveData;
    FragmentManager fm;
    SharedPreferences sharedPreferences;

    SwipeRefreshLayout swipeRefreshLayout;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_thread);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        listView = (ListView) findViewById(R.id.lv_thread);

        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        logEmail = sharedPreferences.getString("logEmail", TAG);
        link = getString(R.string.doc_thread_url);
        fm = getSupportFragmentManager();

        thread = getIntent().getStringExtra("FilePojoThread");
        id = getIntent().getStringExtra("FilePojoDocId");
        sourceRecipient = this.getIntent().getStringExtra("FilePojoSourceRecipient");
        notifId = this.getIntent().getStringExtra("FilePojoId");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Source and recipient: " + sourceRecipient);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(this);



        if(logEmail != null)
        {

            retrieveData = new RetrieveData(getApplicationContext());
            file.clear();
            filePojoArrayList.clear();
            file = retrieveData.getThread(link, thread);

            toolbar.setSubtitle("No. of documents in thread: " + file.size());

            for (int i = 0; i < file.size(); i++) {
                Map<String, String> fileMap = file.get(i);
                FilePojo f = new FilePojo(
                        fileMap.get("createdBy"),
                        fileMap.get("email"),
                        fileMap.get("timeCreated"),
                        fileMap.get("category"),
                        fileMap.get("fileName"),
                        fileMap.get("desc"),
                        fileMap.get("referenceNum"),
                        fileMap.get("actionReq"),
                        fileMap.get("actionDue"),
                        fileMap.get("status"),
                        fileMap.get("title"),
                        fileMap.get("type"),
                        fileMap.get("thread"));
                filePojoArrayList.add(f);
            }

//            paayos nlng ng pag left and right ng thread sa xml and design
            
            ThreadAdapter threadAdapter = new ThreadAdapter(this, filePojoArrayList);
            listView.setAdapter(threadAdapter);
            threadAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Bundle args = new Bundle();

                    args.putString("FilePojoTitle", file.get(position).get("title"));
                    args.putString("FilePojoCategory", file.get(position).get("category"));
                    args.putString("FilePojoActionReq", file.get(position).get("actionReq"));
                    args.putString("FilePojoActionDue", file.get(position).get("actionDue"));
                    args.putString("FilePojoFileName", file.get(position).get("fileName"));
                    args.putString("FilePojoCreatedBy", file.get(position).get("createdBy"));
                    args.putString("FilePojoEmail", file.get(position).get("email"));
                    args.putString("FilePojoFileName", file.get(position).get("fileName"));
                    args.putString("FilePojoTimeCreated", file.get(position).get("timeCreated"));
                    args.putString("FilePojoSourceRecipient", file.get(position).get("sourceRecipient"));
                    args.putString("FilePojoReferenceNumber", file.get(position).get("referenceNum"));
                    args.putString("FilePojoThread", file.get(position).get("thread"));
                    args.putString("FilePojoType", file.get(position).get("type"));
                    args.putString("FilePojoDocId", file.get(position).get("id"));
                    args.putString("FilePojoStatus", file.get(position).get("status"));
                    args.putString("FilePojoDescription", file.get(position).get("desc"));

                    args.putString("FilePojoNotifId", notifId);

                    DocumentOnClickDialogFragment documentOnClickDialogFragment = new DocumentOnClickDialogFragment();
                    documentOnClickDialogFragment.setArguments(args);
                    documentOnClickDialogFragment.show(fm, "RecyclerViewAdapter");
                }
            });
        }
        else
        {
//            palagyan ng dialog box dito.
//            na nakalagay "please log in to continue" tas tsaka magreredirect sa login page natin.


            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            Toast.makeText(getApplicationContext(), "Please log in to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);

        link = getString(R.string.doc_thread_url);
        thread = getIntent().getStringExtra("FilePojoThread");

        retrieveData = new RetrieveData(getApplicationContext());
        file.clear();
        filePojoArrayList.clear();
        file = retrieveData.getThread(link, thread);

        for (int i = 0; i < file.size(); i++) {
            Map<String, String> fileMap = file.get(i);
            FilePojo f = new FilePojo(
                    fileMap.get("createdBy"),
                    fileMap.get("email"),
                    fileMap.get("timeCreated"),
                    fileMap.get("category"),
                    fileMap.get("fileName"),
                    fileMap.get("desc"),
                    fileMap.get("referenceNum"),
                    fileMap.get("actionReq"),
                    fileMap.get("actionDue"),
                    fileMap.get("status"),
                    fileMap.get("title"),
                    fileMap.get("type"),
                    fileMap.get("thread"));
            filePojoArrayList.add(f);
        }

        ThreadAdapter threadAdapter = new ThreadAdapter(this, filePojoArrayList);
        listView.setAdapter(threadAdapter);
        threadAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);


//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);

    }
}

package com.iicsdms.tris.iicsdms2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentsFragment extends Fragment implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {


    Spinner filter;
    SearchView search;
    List<Map<String, String>> file = new ArrayList<Map<String, String>>();
    ArrayList<FilePojo> documents = new ArrayList<>();
    private static final String TAG = "Test";

    String filterValue;
    String link, table, logEmail, linkEmailDept;
    String logDept, logRole, logName;

    RetrieveData retrieveData;

    View rootView;
    RecyclerView recyclerView;
    TextView emptyView;

    Toolbar toolbar;
    SharedPreferences sharedPreferences;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            if (rootView.getParent() != null)
                ((ViewGroup) rootView.getParent()).removeView(rootView);
            return rootView;
        }

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        logEmail = sharedPreferences.getString("logEmail", TAG);

        link = getString(R.string.doc_url);
        linkEmailDept = getString(R.string.email_dept_url);
        rootView = inflater.inflate(R.layout.fragment_documents, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.listRecyclerView);
        filter = (Spinner) rootView.findViewById(R.id.filter);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(this);


        if (!logEmail.equals(TAG)) {

            //------------------------- dropdown --------------------------------------------
            filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filterValue = String.valueOf(filter.getSelectedItem());

                    documents.clear();

                    if (filterValue.equals("All Documents")) {
                        table = "all";
                        file.clear();
                        retrieveData = new RetrieveData(getContext());

                        logDept = retrieveData.getDept(linkEmailDept, logEmail)[0];
                        logRole = retrieveData.getDept(linkEmailDept, logEmail)[1];

                        if (logRole.equals("Faculty")) {
                            file = retrieveData.getDocumentList(link, table, logEmail,
                                    getString(R.string.forward_from).concat(" ").concat(logDept));
                        } else {
                            file = retrieveData.getDocumentList(link, table, logEmail);
                        }


                        for (int i = 0; i < file.size(); i++) {
                            Map<String, String> fileMap = file.get(i);
                            FilePojo f = new FilePojo(
                                    fileMap.get("title"),
                                    fileMap.get("category"),
                                    fileMap.get("createdBy"),
                                    fileMap.get("email"),
                                    fileMap.get("timeCreated"),
                                    fileMap.get("srcRecipient"),
                                    fileMap.get("fileName"),
                                    fileMap.get("thread"),
                                    fileMap.get("type"),
                                    fileMap.get("id"),
                                    fileMap.get("status"),
                                    fileMap.get("id"),
                                    fileMap.get("id"),
                                    fileMap.get("id"));

                            documents.add(f);
                        }

                        if (file.isEmpty() || file == null || file.size() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }

                    } else if (filterValue.equals("Incoming Documents")) {
                        table = "incoming_documents";
                        file.clear();
                        retrieveData = new RetrieveData(getContext());

                        logDept = retrieveData.getDept(linkEmailDept, logEmail)[0];
                        logRole = retrieveData.getDept(linkEmailDept, logEmail)[1];

                        if (logRole.equals("Faculty")) {
                            file = retrieveData.getDocumentList(link, table, logEmail,
                                    getString(R.string.forward_from).concat(" ").concat(logDept));
                        } else {
                            file = retrieveData.getDocumentList(link, table, logEmail);
                        }


                        for (int i = 0; i < file.size(); i++) {
                            Map<String, String> fileMap = file.get(i);
                            FilePojo f = new FilePojo(
                                    fileMap.get("title"),
                                    fileMap.get("srcRecipient"),
                                    fileMap.get("timeCreated"),
                                    fileMap.get("category"),
                                    fileMap.get("actionReq"),
                                    fileMap.get("actionDue"),
                                    fileMap.get("status"),
                                    fileMap.get("refNum"),
                                    fileMap.get("fileName"),
                                    fileMap.get("type"),
                                    fileMap.get("thread"),
                                    fileMap.get("id"));
                            documents.add(f);
                        }

                        if (file.isEmpty() || file == null || file.size() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    } else if (filterValue.equals("Outgoing Documents")) {
                        table = "outgoing_documents";
                        file.clear();
                        retrieveData = new RetrieveData(getContext());
                        file = retrieveData.getDocumentList(link, table, logEmail);

                        for (int i = 0; i < file.size(); i++) {
                            Map<String, String> fileMap = file.get(i);
                            FilePojo f = new FilePojo(
                                    fileMap.get("title"),
                                    fileMap.get("category"),
                                    fileMap.get("timeCreated"),
                                    fileMap.get("srcRecipient"),
                                    fileMap.get("fileName"),
                                    fileMap.get("type"),
                                    fileMap.get("thread"),
                                    fileMap.get("id"));
                            documents.add(f);
                        }

                        if (file.isEmpty() || file == null || file.size() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }


                    } else if (filterValue.equals("Archived Documents")) {
                        table = "archived_documents";
                        file.clear();
                        retrieveData = new RetrieveData(getContext());
                        file = retrieveData.getDocumentList(link, table, logEmail);

                        for (int i = 0; i < file.size(); i++) {
                            Map<String, String> fileMap = file.get(i);
                            FilePojo f = new FilePojo(
                                    fileMap.get("title"),
                                    fileMap.get("type"),
                                    fileMap.get("category"),
                                    fileMap.get("sourceRecipient"),
                                    fileMap.get("createdBy"),
                                    fileMap.get("email"),
                                    fileMap.get("uploadDate"),
                                    fileMap.get("acadYear"),
                                    fileMap.get("fileName"),
                                    fileMap.get("id"),
                                    fileMap.get("folderId"));
                            documents.add(f);
                        }

                        if (file.isEmpty() || file == null || file.size() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    } else if (filterValue.equals("Personal Documents")) {
                        table = "personal_documents";
                        file.clear();
                        retrieveData = new RetrieveData(getContext());
                        file = retrieveData.getDocumentList(link, table, logEmail);

                        for (int i = 0; i < file.size(); i++) {
                            Map<String, String> fileMap = file.get(i);
                            FilePojo f = new FilePojo(
                                    fileMap.get("title"),
                                    fileMap.get("category"),
                                    fileMap.get("timeCreated"),
                                    fileMap.get("fileName"),
                                    fileMap.get("type"),
                                    fileMap.get("id"),
                                    fileMap.get("createdBy"));
                            documents.add(f);
                        }

                        if (file.isEmpty() || file == null || file.size() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }


                    }

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    MyFileRecyclerViewAdapter listAdapter = new MyFileRecyclerViewAdapter(getFragmentManager(), documents, getContext(), logEmail);

                    recyclerView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getContext(),
                    R.array.doc_filter, R.layout.spinner_item);

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            filter.setAdapter(adapter);

        } else {

            BypassDialogFragment bypassDialogFragment = new BypassDialogFragment();
            bypassDialogFragment.show(getFragmentManager(), "DocumentsFragment");

            Intent intent = new Intent(getContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            Toast.makeText(getContext(), "Please log in to continue", Toast.LENGTH_SHORT).show();

        }

        return rootView;

    }
//      ------------------------------ search view of docs -----------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_documents, menu);
        super.onCreateOptionsMenu(menu, inflater);

        if (!logEmail.equals(TAG)) {

            MenuItem item = menu.findItem(R.id.search_action);
            SearchView search = (SearchView) item.getActionView();

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    file.clear();
                    documents.clear();
                    retrieveData = new RetrieveData(getContext());

                    logDept = retrieveData.getDept(linkEmailDept, logEmail)[0];
                    logRole = retrieveData.getDept(linkEmailDept, logEmail)[1];

                    if (logRole.equals("Faculty")) {
                        file = retrieveData.searchDocument(link, table, logEmail, newText,
                                getString(R.string.forward_from).concat(" ").concat(logDept));
                    } else {
                        file = retrieveData.searchDocument(link, table, logEmail, newText);
                    }


                    if (!file.isEmpty()) {

                        if (table.equals("personal_documents")) {
                            for (int i = 0; i < file.size(); i++) {
                                Map<String, String> fileMap = file.get(i);
                                FilePojo f = new FilePojo(
                                        fileMap.get("title"),
                                        fileMap.get("category"),
                                        fileMap.get("timeCreated"),
                                        fileMap.get("fileName"),
                                        fileMap.get("type"),
                                        fileMap.get("id"),
                                        fileMap.get("createdBy"));
                                documents.add(f);
                            }

                            if (file.isEmpty() || file == null || file.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                            }

                        } else if (table.equals("archived_documents")) {
                            for (int i = 0; i < file.size(); i++) {
                                Map<String, String> fileMap = file.get(i);
                                FilePojo f = new FilePojo(
                                        fileMap.get("title"),
                                        fileMap.get("type"),
                                        fileMap.get("category"),
                                        fileMap.get("sourceRecipient"),
                                        fileMap.get("createdBy"),
                                        fileMap.get("email"),
                                        fileMap.get("uploadDate"),
                                        fileMap.get("acadYear"),
                                        fileMap.get("fileName"),
                                        fileMap.get("id"),
                                        fileMap.get("folderId"));
                                documents.add(f);
                            }

                            if (file.isEmpty() || file == null || file.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                            }

                        } else if (table.equals("outgoing_documents")) {
                            for (int i = 0; i < file.size(); i++) {
                                Map<String, String> fileMap = file.get(i);
                                FilePojo f = new FilePojo(
                                        fileMap.get("title"),
                                        fileMap.get("category"),
                                        fileMap.get("timeCreated"),
                                        fileMap.get("srcRecipient"),
                                        fileMap.get("fileName"),
                                        fileMap.get("type"),
                                        fileMap.get("thread"),
                                        fileMap.get("id"));
                                documents.add(f);
                            }

                            if (file.isEmpty() || file == null || file.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                            }

                        } else if (table.equals("incoming_documents")) {
                            for (int i = 0; i < file.size(); i++) {
                                Map<String, String> fileMap = file.get(i);
                                FilePojo f = new FilePojo(
                                        fileMap.get("title"),
                                        fileMap.get("srcRecipient"),
                                        fileMap.get("timeCreated"),
                                        fileMap.get("category"),
                                        fileMap.get("actionReq"),
                                        fileMap.get("actionDue"),
                                        fileMap.get("status"),
                                        fileMap.get("refNum"),
                                        fileMap.get("fileName"),
                                        fileMap.get("type"),
                                        fileMap.get("thread"),
                                        fileMap.get("id"));
                                documents.add(f);
                            }

                            if (file.isEmpty() || file == null || file.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                            }

                        } else if (table.equals("all")) {
                            for (int i = 0; i < file.size(); i++) {
                                Map<String, String> fileMap = file.get(i);
                                FilePojo f = new FilePojo(
                                        fileMap.get("title"),
                                        fileMap.get("category"),
                                        fileMap.get("createdBy"),
                                        fileMap.get("email"),
                                        fileMap.get("timeCreated"),
                                        fileMap.get("srcRecipient"),
                                        fileMap.get("fileName"),
                                        fileMap.get("thread"),
                                        fileMap.get("type"),
                                        fileMap.get("id"),
                                        fileMap.get("status"),
                                        fileMap.get("id"),
                                        fileMap.get("id"),
                                        fileMap.get("id"));
                                documents.add(f);
                            }

                            if (file.isEmpty() || file == null || file.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                            }


                        } else {
                            Toast.makeText(getContext(), "No documents available.", Toast.LENGTH_LONG).show();
                        }

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        MyFileRecyclerViewAdapter listAdapter = new MyFileRecyclerViewAdapter(getFragmentManager(), documents, getContext(), logEmail);
//                        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//                        recyclerView.addItemDecoration(itemDecoration);
                        recyclerView.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();
                    }

                    return false;
                }

            });

        } else {
            BypassDialogFragment bypassDialogFragment = new BypassDialogFragment();
            bypassDialogFragment.show(getFragmentManager(), "DocumentsFragment");

            Intent intent = new Intent(getContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            Toast.makeText(getContext(), "Please log in to continue", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_action:
        }

        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        documents.clear();

        if (filterValue.equals("All Documents")) {
            table = "all";
            file.clear();
            retrieveData = new RetrieveData(getContext());

            logDept = retrieveData.getDept(linkEmailDept, logEmail)[0];
            logRole = retrieveData.getDept(linkEmailDept, logEmail)[1];

            if (logRole.equals("Faculty")) {
                file = retrieveData.getDocumentList(link, table, logEmail,
                        getString(R.string.forward_from).concat(" ").concat(logDept));
            } else {
                file = retrieveData.getDocumentList(link, table, logEmail);
            }


            for (int i = 0; i < file.size(); i++) {
                Map<String, String> fileMap = file.get(i);
                FilePojo f = new FilePojo(
                        fileMap.get("title"),
                        fileMap.get("category"),
                        fileMap.get("createdBy"),
                        fileMap.get("email"),
                        fileMap.get("timeCreated"),
                        fileMap.get("srcRecipient"),
                        fileMap.get("fileName"),
                        fileMap.get("thread"),
                        fileMap.get("type"),
                        fileMap.get("id"),
                        fileMap.get("status"),
                        fileMap.get("id"),
                        fileMap.get("id"),
                        fileMap.get("id"));

                documents.add(f);
                swipeRefreshLayout.setRefreshing(false);
            }

            if (file.isEmpty() || file == null || file.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }

        } else if (filterValue.equals("Incoming Documents")) {
            table = "incoming_documents";
            file.clear();
            retrieveData = new RetrieveData(getContext());

            logDept = retrieveData.getDept(linkEmailDept, logEmail)[0];
            logRole = retrieveData.getDept(linkEmailDept, logEmail)[1];

            if (logRole.equals("Faculty")) {
                file = retrieveData.getDocumentList(link, table, logEmail,
                        getString(R.string.forward_from).concat(" ").concat(logDept));
            } else {
                file = retrieveData.getDocumentList(link, table, logEmail);
            }


            for (int i = 0; i < file.size(); i++) {
                Map<String, String> fileMap = file.get(i);
                FilePojo f = new FilePojo(
                        fileMap.get("title"),
                        fileMap.get("srcRecipient"),
                        fileMap.get("timeCreated"),
                        fileMap.get("category"),
                        fileMap.get("actionReq"),
                        fileMap.get("actionDue"),
                        fileMap.get("status"),
                        fileMap.get("refNum"),
                        fileMap.get("fileName"),
                        fileMap.get("type"),
                        fileMap.get("thread"),
                        fileMap.get("id"));
                documents.add(f);
                swipeRefreshLayout.setRefreshing(false);
            }

            if (file.isEmpty() || file == null || file.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        } else if (filterValue.equals("Outgoing Documents")) {
            table = "outgoing_documents";
            file.clear();
            retrieveData = new RetrieveData(getContext());
            file = retrieveData.getDocumentList(link, table, logEmail);

            for (int i = 0; i < file.size(); i++) {
                Map<String, String> fileMap = file.get(i);
                FilePojo f = new FilePojo(
                        fileMap.get("title"),
                        fileMap.get("category"),
                        fileMap.get("timeCreated"),
                        fileMap.get("srcRecipient"),
                        fileMap.get("fileName"),
                        fileMap.get("type"),
                        fileMap.get("thread"),
                        fileMap.get("id"));
                documents.add(f);
                swipeRefreshLayout.setRefreshing(false);
            }

            if (file.isEmpty() || file == null || file.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }


        } else if (filterValue.equals("Archived Documents")) {
            table = "archived_documents";
            file.clear();
            retrieveData = new RetrieveData(getContext());
            file = retrieveData.getDocumentList(link, table, logEmail);

            for (int i = 0; i < file.size(); i++) {
                Map<String, String> fileMap = file.get(i);
                FilePojo f = new FilePojo(
                        fileMap.get("title"),
                        fileMap.get("type"),
                        fileMap.get("category"),
                        fileMap.get("sourceRecipient"),
                        fileMap.get("createdBy"),
                        fileMap.get("email"),
                        fileMap.get("uploadDate"),
                        fileMap.get("acadYear"),
                        fileMap.get("fileName"),
                        fileMap.get("id"),
                        fileMap.get("folderId"));
                documents.add(f);
                swipeRefreshLayout.setRefreshing(false);
            }

            if (file.isEmpty() || file == null || file.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        } else if (filterValue.equals("Personal Documents")) {
            table = "personal_documents";
            file.clear();
            retrieveData = new RetrieveData(getContext());
            file = retrieveData.getDocumentList(link, table, logEmail);

            for (int i = 0; i < file.size(); i++) {
                Map<String, String> fileMap = file.get(i);
                FilePojo f = new FilePojo(
                        fileMap.get("title"),
                        fileMap.get("category"),
                        fileMap.get("timeCreated"),
                        fileMap.get("fileName"),
                        fileMap.get("type"),
                        fileMap.get("id"),
                        fileMap.get("createdBy"));
                documents.add(f);
                swipeRefreshLayout.setRefreshing(false);
            }

            if (file.isEmpty() || file == null || file.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }


        }

        MyFileRecyclerViewAdapter listAdapter = new MyFileRecyclerViewAdapter(getFragmentManager(), documents, getContext(), logEmail);
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

    }
}

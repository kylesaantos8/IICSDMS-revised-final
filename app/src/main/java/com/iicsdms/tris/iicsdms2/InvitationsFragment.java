package com.iicsdms.tris.iicsdms2;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvitationsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
//    private static final String TAG = "InvitationsFragment";

    String link, logEmail;

    List<Map<String,String>> file = new ArrayList<Map<String, String>>();
    ArrayList<InvitePojo> invitationsList = new ArrayList<>();

    private RecyclerView recyclerView;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragments_invitations,container,false);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        link = getString(R.string.invite_url);
        logEmail = getActivity().getIntent().getStringExtra("logEmail");

        recyclerView = (RecyclerView) view.findViewById(R.id.listInvite);

        RetrieveData retrieveData = new RetrieveData(getContext());
//        file = retrieveData.getFileInvitation(link, logEmail);

        invitationsList.clear();
        for(int i = 0; i < file.size(); i++){
            Map<String,String> fileMap = file.get(i);
            InvitePojo f = new InvitePojo(
                    fileMap.get("eventTitle"),
                    fileMap.get("eventLoc"),
                    fileMap.get("eventStart"),
                    fileMap.get("eventEnd"),
                    fileMap.get("eventDesc"),
                    fileMap.get("eventId"),
                    fileMap.get("eventAllDay"),
                    fileMap.get("eventCreatedBy"),
                    fileMap.get("eventStatus"),
                    fileMap.get("eventResponse"),
                    fileMap.get("eventDateResponse"));
            invitationsList.add(f);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        MyInviteRecyclerViewAdapter listAdapter = new MyInviteRecyclerViewAdapter(getFragmentManager(), invitationsList, getContext(), logEmail);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

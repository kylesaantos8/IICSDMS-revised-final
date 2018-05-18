package com.iicsdms.tris.iicsdms2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Tris on 27/03/2018.
 */

public class CalendarAndInviteFragment extends Fragment{

    private FragmentTabHost mTabHost;

    //Mandatory Constructor
    public CalendarAndInviteFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tabs,container, false);


        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
//        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
//
//        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Calendar"),
//                CalendarFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("Invites"),
//                InvitationsFragment.class, null);
//
//        return rootView;
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        View tabIndicatorToday = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        ((TextView) tabIndicatorToday.findViewById(R.id.tv_tab_txt)).setText("Calendar");
        mTabHost.addTab(mTabHost.newTabSpec("Today").setIndicator(tabIndicatorToday), CalendarFragment.class, null);
        View tabIndicatorLive = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        ((TextView) tabIndicatorLive.findViewById(R.id.tv_tab_txt)).setText("Invites");
        mTabHost.addTab(mTabHost.newTabSpec("Live").setIndicator(tabIndicatorLive), InvitationsFragment.class, null);
        return rootView;

    }

}

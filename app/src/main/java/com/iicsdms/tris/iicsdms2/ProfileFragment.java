package com.iicsdms.tris.iicsdms2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class ProfileFragment extends Fragment {

    private TextView tvEmail, tvFacNum, tvName, tvCnum, tvDept;
    private Button buttonLogout;

    private static final String TAG = "Test";

    String logEmail;
    String link, logsLogLink;

    RetrieveData retrieveData;
    ProfilePojo profilePojo = new ProfilePojo();

    Toolbar toolbar;

    View rootView;
    SharedPreferences sharedPreferences;
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
//        logEmail = getActivity().getIntent().getStringExtra("logEmail");

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Profile");
        setHasOptionsMenu(true);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        link = getString(R.string.profile_url);
        logsLogLink = getString(R.string.logs_log);

        if(!logEmail.equals(TAG))
        {

            retrieveData = new RetrieveData(getContext());
            profilePojo = retrieveData.getFileProfile(link, logEmail);

            tvEmail = (TextView) rootView.findViewById(R.id.email);
            tvFacNum = (TextView) rootView.findViewById(R.id.faculty);
            tvName = (TextView) rootView.findViewById(R.id.fullName);
            tvCnum = (TextView) rootView.findViewById(R.id.contact);
            tvDept = (TextView) rootView.findViewById(R.id.department);

            tvEmail.setText(logEmail);

            if(profilePojo.getFacNumber() == null || profilePojo.getFacNumber().equals("null") ||
                    profilePojo.getFacNumber().equals("NULL") || profilePojo.getFacNumber().isEmpty())
            {
                tvFacNum.setText("Faculty Number");
            }
            else
            {
                tvFacNum.setText(profilePojo.getFacNumber());
            }

            if(profilePojo.getTitle() == null || profilePojo.getTitle().equals("null") ||
                    profilePojo.getTitle().equals("NULL") || profilePojo.getTitle().isEmpty() ||
                    profilePojo.getName() == null || profilePojo.getName().equals("null") ||
                    profilePojo.getName().equals("NULL") || profilePojo.getName().isEmpty())
            {
                tvName.setText("Title.".concat(" ").concat("Full Name"));
            }
            else
            {
                tvName.setText(profilePojo.getTitle().concat(" ").concat(profilePojo.getName()));
            }

            if(profilePojo.getDepartment() == null || profilePojo.getDepartment().equals("null") ||
                    profilePojo.getDepartment().equals("NULL") || profilePojo.getDepartment().isEmpty())
            {
                tvDept.setText("Department");
            }
            else
            {
                tvDept.setText(profilePojo.getDepartment());
            }
            if (profilePojo.getContactNumber() == null ||
                    profilePojo.getContactNumber().isEmpty() ||
                    profilePojo.getContactNumber().equals("null") ||
                    profilePojo.getContactNumber().equals("NULL")) {
                tvCnum.setText("Contact Number");
            } else {
                tvCnum.setText("*******".concat(profilePojo.getContactNumber().substring(7, 11)));
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

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_profile, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout_action:
                LogoutOnClickDialogFragment logoutOnClickDialogFragment = new LogoutOnClickDialogFragment();
                logoutOnClickDialogFragment.show(getFragmentManager(), "ProfileFragment" );
                return true;
        }
        return false;
    }
}
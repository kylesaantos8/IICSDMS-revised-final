package com.iicsdms.tris.iicsdms2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tris on 03/03/2018.
 */

public class LogoutOnClickDialogFragment extends AppCompatDialogFragment{

    static final String TAG = "Test";
    String logEmail;
    String link, logsLogLink;

    RetrieveData retrieveData;
    ProfilePojo profilePojo = new ProfilePojo();

    SharedPreferences sharedPreferences;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        logEmail = sharedPreferences.getString("logEmail", TAG);
        link = getString(R.string.profile_url);
        logsLogLink = getString(R.string.logs_log);

        retrieveData = new RetrieveData(getContext());
        profilePojo = retrieveData.getFileProfile(link, logEmail);

        CharSequence[] sequence = {"Yes","No"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to log out?")

//                .setItems(sequence, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//                        switch (which){
//                            case 0:
//                                Intent intent = new Intent(getContext(), Login.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                                Logs logs = new Logs();
//                                logs.Log(logsLogLink, getString(R.string.type), getString(R.string.logout),
//                                        profilePojo.getName().concat(" (").concat(logEmail).concat(")"),
//                                        profilePojo.getUserType(), profilePojo.getDepartment());
//                                startActivity(intent);
//
//                                Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
//                                break;
//
//                            case 1:
//
//
//                                break;
//                        }
//                    }
//                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getActivity().stopService(new Intent(getContext(), NotificationService.class));

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(getContext(), Login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                Logs logs = new Logs();
                                logs.Log(logsLogLink, getString(R.string.type), getString(R.string.logout),
                                        profilePojo.getName().concat(" (").concat(logEmail).concat(")"),
                                        profilePojo.getUserType(), profilePojo.getDepartment());
                                startActivity(intent);

                                Toast.makeText(getContext(), "You have logged out from the system.", Toast.LENGTH_SHORT).show();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}

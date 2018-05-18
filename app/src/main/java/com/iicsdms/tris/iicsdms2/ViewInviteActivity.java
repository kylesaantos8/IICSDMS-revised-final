package com.iicsdms.tris.iicsdms2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tris on 05/03/2018.
 */

public class ViewInviteActivity extends AppCompatActivity {

    static final String TAG = "Test";
    TextView inviteTitle, inviteVenue, inviteStart, inviteEnd, inviteDetails, inviteStatus, inviteResponse;

    Toolbar toolbar;

    TextView textTitle;
    Button acc, dec;

    String link, linkSeen, linkLog, linkLogs, logEmail;
    String logResp;
    String id, title, loc, allDay, start, end, desc, createdBy, status, response, dateResponse;
    boolean allDayBool;
    String [] startingDate, endingDate, startDateArr, startTimeArr, endDateArr, endTimeArr;
    String startDate, startTime, startYr, startMon, startDay, startHr, startMin, startSec;
    int startMonth, endMonth;
    String endDate, endTime, endYr, endMon, endDay, endHr, endMin, endSec;
    InvitePojo invitePojo = new InvitePojo();
    SeenStatus seenStatus = new SeenStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_info);

        String fileTitle = getIntent().getStringExtra("inviteTitle");
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(fileTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textTitle = (TextView) findViewById(R.id.tv1);
        acc = (Button) findViewById(R.id.accept);
        dec = (Button) findViewById(R.id.decline);

        link = getString(R.string.invite_response_url);
        linkSeen = getString(R.string.invite_seen_url);
        linkLog = getString(R.string.logs_url);
        linkLogs = getString(R.string.logs_log);
        logEmail = getIntent().getStringExtra("logEmail");

        id = getIntent().getStringExtra("inviteId");
        title = getIntent().getStringExtra("inviteTitle");
        loc = getIntent().getStringExtra("inviteLoc");
        start = getIntent().getStringExtra("inviteStart");
        end = getIntent().getStringExtra("inviteEnd");
        desc = getIntent().getStringExtra("inviteDescription");
        response = getIntent().getStringExtra("inviteResponse");

        allDay = getIntent().getStringExtra("inviteAllDay");
        createdBy = getIntent().getStringExtra("inviteCreatedBy");
        status = getIntent().getStringExtra("inviteStatus");
        dateResponse = getIntent().getStringExtra("inviteDateResponse");

        if(allDay.equals("1"))
        {
            allDayBool = true;
        }
        else if(allDay.equals("0"))
        {
            allDayBool = false;
        }

        startingDate = start.split(" ");
        startDate = startingDate[0];
        startTime = startingDate[1];

        startDateArr = startDate.split("-");
        startYr = startDateArr[0];
        startMonth = Integer.parseInt(startDateArr[1])-1;
        startMon = String.valueOf(startMonth);
        startDay = startDateArr[2];

        startTimeArr = startTime.split(":");
        startHr = startTimeArr[0];
        startMin = startTimeArr[1];
        startSec = startTimeArr[2];



        endingDate = end.split(" ");
        endDate = endingDate[0];
        endTime = endingDate[1];

        endDateArr = endDate.split("-");
        endYr = endDateArr[0];
        endMonth = Integer.parseInt(endDateArr[1])-1;
        endMon = String.valueOf(endMonth);
        endDay = endDateArr[2];

        endTimeArr = endTime.split(":");
        endHr = endTimeArr[0];
        endMin = endTimeArr[1];
        endSec = endTimeArr[2];

        inviteTitle = (TextView) findViewById(R.id.inviteTitle);
        inviteVenue = (TextView) findViewById(R.id.inviteVenue);
        inviteStart = (TextView) findViewById(R.id.inviteStartTime);
        inviteEnd = (TextView) findViewById(R.id.inviteEndTime);
        inviteDetails = (TextView) findViewById(R.id.inviteDetails);
        inviteStatus = (TextView) findViewById(R.id.inviteStatus);
        inviteResponse = (TextView) findViewById(R.id.inviteResponse);

        inviteTitle.setText(title);
        inviteVenue.setText(loc);
        inviteStart.setText(start);
        inviteEnd.setText(end);
        inviteDetails.setText(desc);
        inviteStatus.setText(status);
        if(response.equals("null") || response == null)
        {
            response = "No Response";
            inviteResponse.setText(response);
        }
        else
        {
            inviteResponse.setText(response);
        }


        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logResp = seenStatus.responseSeen(link,id, logEmail);
                String resp = "Accepted";

                if(logResp.equals("No Response") || logResp == null)
                {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    seenStatus.response(linkSeen, id, logEmail, resp, timeStamp);

                    Logs logs = new Logs();
                    invitePojo = logs.LogsInvite(linkLog,logEmail);

//                    logs.Log(linkLogs, getString(R.string.type),
//                            getString(R.string.inv_resp1).concat(" ").concat(resp).concat(" ")
//                                    .concat(getString(R.string.inv_resp2)).concat(" ").concat(title),
//                            invitePojo.getFullName().concat(" (").concat(logEmail).concat(")"),
//                            invitePojo.getType(), invitePojo.getDept());

                    Calendar calendarStart = Calendar.getInstance();
                    calendarStart.set(Integer.parseInt(startYr), Integer.parseInt(startMon),Integer.parseInt(startDay),
                            Integer.parseInt(startHr), Integer.parseInt(startMin));

                    Calendar calendarEnd = Calendar.getInstance();
                    calendarEnd.set(Integer.parseInt(endYr), Integer.parseInt(endMon), Integer.parseInt(endDay),
                            Integer.parseInt(endHr), Integer.parseInt(endMin));

                    Intent calInput = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI);

                    calInput.putExtra(CalendarContract.Events.TITLE, title);
                    calInput.putExtra(CalendarContract.Events.EVENT_LOCATION, loc);
                    calInput.putExtra(CalendarContract.Events.DESCRIPTION, desc);
                    calInput.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendarStart.getTimeInMillis());


                    calInput.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendarEnd.getTimeInMillis());
                    calInput.putExtra(CalendarContract.Events.ALL_DAY, allDayBool);
                    startActivity(calInput);

                    Toast.makeText(getApplicationContext(), "You have " + resp + " this invite", Toast.LENGTH_SHORT).show();
                }
                else if(logResp.equals("Accepted") || logResp.equals("Declined"))
                {
                    Toast.makeText(getApplicationContext(), "You have already " + logResp + " this invite.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logResp = seenStatus.responseSeen(link,id, logEmail);
                String resp = "Declined";

                if(logResp.equals("No Response") || logResp == null)
                {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    seenStatus.response(linkSeen, id, logEmail, resp, timeStamp);

                    Logs logs = new Logs();
                    invitePojo = logs.LogsInvite(linkLog,logEmail);

//                    logs.Log(linkLogs, getString(R.string.type),
//                            getString(R.string.inv_resp1).concat(" ").concat(resp).concat(" ")
//                                    .concat(getString(R.string.inv_resp2)).concat(" ").concat(title),
//                            invitePojo.getFullName().concat(" (").concat(logEmail).concat(")"),
//                            invitePojo.getType(), invitePojo.getDept());

                    Toast.makeText(getApplicationContext(), "You have " + resp + " this invite", Toast.LENGTH_SHORT).show();
                }
                else if(logResp.equals("Accepted") || logResp.equals("Declined"))
                {
                    Toast.makeText(getApplicationContext(), "You have already " + logResp + " this invite.", Toast.LENGTH_SHORT).show();
                }

            }
        });
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

}

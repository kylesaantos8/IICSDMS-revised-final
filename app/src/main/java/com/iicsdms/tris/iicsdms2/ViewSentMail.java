package com.iicsdms.tris.iicsdms2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewSentMail extends AppCompatActivity {

    static final String test = "Test";
    List<Map<String,String>> file = new ArrayList<Map<String, String>>();
    String link, logEmail;
    String mailSubj, mailUser, mailUserEmail, mailType, mailTime, mailId, mailIso, mailAcadYear;

    TextView sentBy, sentSubj, sentType, sentTime, sentIso, sentAcadYear;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sent_mail);

        link = getString(R.string.sent_mail_url);
        logEmail = getIntent().getStringExtra("logEmail");

        mailSubj = getIntent().getStringExtra("mailSubject");
        mailUser = getIntent().getStringExtra("mailUser");
        mailUserEmail = getIntent().getStringExtra("mailUserEmail");
        mailType = getIntent().getStringExtra("mailType");
        mailTime = getIntent().getStringExtra("mailTime");
        mailIso = getIntent().getStringExtra("mailIso");
        mailAcadYear = getIntent().getStringExtra("mailAcadYear");

        mailId = getIntent().getStringExtra("mailId");

        sentBy = (TextView) findViewById(R.id.sent_by);
        sentSubj = (TextView) findViewById(R.id.sent_subj);
        sentType = (TextView) findViewById(R.id.sent_type);
        sentAcadYear = (TextView) findViewById(R.id.sent_yr);
        sentIso = (TextView) findViewById(R.id.sent_iso);
        sentTime = (TextView) findViewById(R.id.sent_time);

        sentBy.setText(mailUser.concat(" (").concat(mailUserEmail).concat(")"));
        sentSubj.setText(mailSubj);
        sentType.setText(mailType);
        sentAcadYear.setText(mailAcadYear);
        sentIso.setText(mailIso);
        sentTime.setText(mailTime);

        RetrieveData retrieveData = new RetrieveData(getApplicationContext());
//        file = retrieveData.getSent(link, mailId);

        listView = (ListView) findViewById(R.id.lv_sent_mail);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, file, R.layout.list_sent,
                new String [] {"otherRecipient", "ack", "timeAck"},
                new int[] {R.id.sent_name, R.id.sent_status, R.id.sent_ack_time});
        listView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }
}

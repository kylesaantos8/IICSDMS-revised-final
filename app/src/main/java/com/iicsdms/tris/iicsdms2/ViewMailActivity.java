package com.iicsdms.tris.iicsdms2;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tris on 04/03/2018.
 */

public class ViewMailActivity extends AppCompatActivity {

    TextView mailUser, mailIso, mailSubject, mailType, mailTime, mailYr;

    String mailId;

    Button download, ack;
    String link, linkSeen, linkSeenUpdate, linkLogsDtls, linkLogs, logEmail, time, date, timeStamp,
            mailUserN, mailEmail, mailIsoN, mailSubj, mailMailType, mailYear, mailAckn;
    byte[] data1;

    Toolbar toolbar;

    private static final String TAG = "Testing";

    PDFView pdfView;
    SeenStatus seenStatus;

    String[] timeStampArray;

    NotifPojo notifPojo = new NotifPojo();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_mail);

        String mailTitle = getIntent().getStringExtra("mailSubject");
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mailTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        link = getString(R.string.inbox_view_url);
        linkSeen = getString(R.string.seen_url);
        linkSeenUpdate = getString(R.string.seen_update_url1);
        linkLogsDtls = getString(R.string.logs_url);
        linkLogs = getString(R.string.logs_log);
        logEmail = getIntent().getStringExtra("logEmail");

        mailId = getIntent().getStringExtra("mailId");
        timeStamp = getIntent().getStringExtra("mailTime");
        mailUserN = getIntent().getStringExtra("mailUser");
        mailEmail = getIntent().getStringExtra("mailUserEmail");
        mailIsoN = getIntent().getStringExtra("mailIso");
        mailSubj = getIntent().getStringExtra("mailSubject");
        mailAckn = getIntent().getStringExtra("mailAck");
        mailMailType = getIntent().getStringExtra("mailType");
        mailYear = getIntent().getStringExtra("mailAcadYear");

        download = (Button) findViewById(R.id.download);
        ack = (Button) findViewById(R.id.ack);

        pdfView = (PDFView) findViewById(R.id.pdf);


        timeStampArray = timeStamp.split(" ");
        time = timeStampArray[1];
        date = timeStampArray[0];


        mailUser = (TextView) findViewById(R.id.mailUser);
        mailIso = (TextView) findViewById(R.id.mailIso);
        mailSubject = (TextView) findViewById(R.id.mailSubject);
        mailType = (TextView) findViewById(R.id.mailType);
        mailTime = (TextView) findViewById(R.id.mailTime);
        mailYr = (TextView) findViewById(R.id.mailYr);



        mailUser.setText(mailUserN + " (" + mailEmail + ") ");
        mailSubject.setText(mailSubj);
        mailIso.setText(mailIsoN);
        mailType.setText(mailMailType);
        mailTime.setText(date + " at " + time);
        mailYr.setText(mailYear);



        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
            }
        });

        ack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seenStatus = new SeenStatus();
                if(seenStatus.seen(linkSeen, mailId, logEmail).equals("Acknowledged"))
                {
                    Toast.makeText(getApplicationContext(), "This mail has already been acknowledged.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String update = "Acknowledged";
                    seenStatus.seenUpdate(linkSeenUpdate, mailId, update, timeStamp, logEmail);

                    Logs logs = new Logs();
                    notifPojo = logs.Logs(linkLogsDtls, logEmail);

//                    logs.Log(linkLogs, getString(R.string.type), getString(R.string.mail_ack).concat(mailSubj),
//                            notifPojo.getFullName().concat(" (").concat(logEmail).concat(")"),
//                            notifPojo.getUserType(), notifPojo.getDept());

                    Toast.makeText(getApplicationContext(), "This mail has been acknowledged.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RetrieveData retrieveData = new RetrieveData(getApplicationContext());
//        data1 = retrieveData.getFileInboxData(link,mailId);

//      ------------------------------ pdf viewer --------------------------------------------------
        pdfView.fromBytes(data1)
                .defaultPage(0)
                .enableAntialiasing(true)
                .enableSwipe(true)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();

//            by: Bartosz Schiller (barteksc)
//            link: https://github.com/barteksc/AndroidPdfViewer

//      --------------------------------------------------------------------------------------------

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

    public void writeToFile(String data)
    {
        final File path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + File.separator + "ICS DMS");

        if(!path.exists())
        {
            path.mkdirs();
        }

        final File file = new File(path, "test.pdf");

        try
        {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

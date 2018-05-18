package com.iicsdms.tris.iicsdms2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.security.AccessController.getContext;

/**
 * Created by Tris on 03/03/2018.
 */

public class ViewFileInfoActivity extends AppCompatActivity {

    static final String TAG = "TAG";
    TextView fileTitle, fileCreatedBy, fileFileName, fileTimeCreated, fileType, fileFromTo, fileTimeRecTimeSent, fileDescStatus,
                fileActionDue , fileActionRequired, fileReferenceNo, fileDesc, fileStatus, fileRefNoUploadBy, fileSrcRecipient,
                fileActionDueHolder, fileActionRequiredHolder, fileCategory, notifStatus, notifStatusHolder;

    Toolbar toolbar;
    FilePojo filePojo;
    RetrieveData retrieveData;
    SeenStatus seenStatus;
    SharedPreferences sharedPreferences;
    private List<Map<String,String>> file = new ArrayList<Map<String, String>>();
    private List<String> notifSentId = new ArrayList<String>();
    private List<Map<String,String>> id = new ArrayList<Map<String, String>>();
    private List<Map<String,String>> notif = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_info);
        String seenLink = getString(R.string.seen_track_url);
        String seenNameLink = getString(R.string.seen_track_name_url);
        String title = getIntent().getStringExtra("fileTitle");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String emailDeptLink = getString(R.string.email_dept2_url);
        String notifyIdLink = getString(R.string.notif_id_url);
        String getDeptLink = getString(R.string.get_dept_url);

        fileRefNoUploadBy = (TextView) findViewById(R.id.refnouploadby);
        fileReferenceNo = (TextView) findViewById(R.id.reference);
        fileSrcRecipient = (TextView) findViewById(R.id.source_recipient);
        fileFromTo = (TextView) findViewById(R.id.fromto);
        fileCreatedBy = (TextView) findViewById(R.id.created_by);
        fileFileName = (TextView) findViewById(R.id.file_name);
        fileTimeRecTimeSent = (TextView) findViewById(R.id.timerectimesent);
        fileTimeCreated = (TextView) findViewById(R.id.time_stamp);
        fileType = (TextView) findViewById(R.id.type);
        fileCategory = (TextView) findViewById(R.id.category);
        fileDescStatus = (TextView) findViewById(R.id.descstatus);
        fileDesc = (TextView) findViewById(R.id.desc);
        fileStatus = (TextView) findViewById(R.id.status);
        fileActionDueHolder = (TextView) findViewById(R.id.action_due_holder);
        fileActionRequiredHolder = (TextView) findViewById(R.id.action_required_holder);
        fileActionDue = (TextView) findViewById(R.id.action_due);
        fileActionRequired = (TextView) findViewById(R.id.action_required);
        notifStatus = (TextView) findViewById(R.id.notif_status);
        notifStatusHolder = (TextView) findViewById(R.id.notif_status_holder);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        String logEmail = sharedPreferences.getString("logEmail", TAG);

        String link = getString(R.string.doc_info_url);

        String fileThread = this.getIntent().getStringExtra("fileThread");
        String fileId = getIntent().getStringExtra("fileId");
        String fileFolderId = getIntent().getStringExtra("fileFolderId");

        String fileref = this.getIntent().getStringExtra("fileReferenceNum");
        String filesourceRecipient = this.getIntent().getStringExtra("fileSourceRecipient");
        String filecreatedBy = this.getIntent().getStringExtra("fileCreatedBy");
        String filefileName = this.getIntent().getStringExtra("fileFileName");
        String filetimeCreated = this.getIntent().getStringExtra("fileTimeCreated");
        String filetype = this.getIntent().getStringExtra("fileType");
        String fileactionDue = this.getIntent().getStringExtra("fileActionDue");
        String fileactionReq = this.getIntent().getStringExtra("fileActionReq");
        String filestatus= this.getIntent().getStringExtra("fileStatus");
        String filecategory = this.getIntent().getStringExtra("fileCategory");
        String filedesc = this.getIntent().getStringExtra("fileDescription");
        String filenote, filedept;

        String fileNotifId = this.getIntent().getStringExtra("fileNotifId");
        String fileNotifSentence = null;




        if(filetype.equals("Personal"))
        {
            retrieveData = new RetrieveData(getApplicationContext());
            filePojo = retrieveData.getDocumentInfo(link, logEmail, filetype, fileId, fileThread, fileFolderId);

//            Log.e(TAG, filePojo.getDescription());
//            Log.e(TAG, filePojo.getCreatedBy());

            filedesc = filePojo.getDescription();
            filecreatedBy = filePojo.getCreatedBy();
        }
        else if(filetype.equals("Incoming"))
        {
            if(fileThread != null)
            {
                retrieveData = new RetrieveData(getApplicationContext());
                filePojo = retrieveData.getDocumentInfo(link, logEmail, filetype, fileId, fileThread, fileFolderId);

//                Log.e(TAG, filePojo.getDescription());
//                Log.e(TAG, filePojo.getDepartment());
//                Log.e(TAG, filePojo.getNote());

                filedesc = filePojo.getDescription();
                filedept = filePojo.getDepartment();
                filenote = filePojo.getNote();

                String logDept = this.getIntent().getStringExtra("logDept");
                String [] notif;
                String notifSentence = null;
                String notifId = null;
                String [] notifIdArr = new String[0];

                if(fileNotifId == null)
                {
                    if(filestatus.contains(getString(R.string.forward_from)))
                    {
                        notif = filestatus.split(": ");

                        if(notif[1].equals("All"))
                        {
                            notifSentence = filecreatedBy + " " + getString(R.string.notif_inc_forward) + " " + title;
                            id = retrieveData.notifyId(notifyIdLink, notifSentence);

                            Log.e(TAG, String.valueOf(id));

                            for(int i = 0; i< id.size(); i++)
                            {
                                if(id.get(i).get("id") != null)
                                {
                                    notifSentId.add(id.get(i).get("id"));
                                }
                            }

                            if(notifSentId.size() == 1)
                            {
                                fileNotifId = notifSentId.get(0);

                                seenStatus = new SeenStatus();
                                file = seenStatus.getNotif(seenLink, fileNotifId);

                                for(int i = 0; i < file.size(); i++)
                                {
                                    fileNotifSentence = file.get(i).get("notifEmail")+ ", " + fileNotifSentence;
                                }

                                if(fileNotifSentence.contains("null"))
                                {
                                    fileNotifSentence = fileNotifSentence.replace(", null", "");
                                    notifStatus.setText(fileNotifSentence);
                                }
                                else
                                {
                                    notifStatus.setText(fileNotifSentence);
                                }
                            }
                            else if (notifSentId.size() > 1)
                            {
                                for(int i = 1; i < notifSentId.size(); i++)
                                {
                                    seenStatus = new SeenStatus();
                                    file = seenStatus.getNotif(seenLink, String.valueOf(notifSentId.get(i)));
                                    Log.e(TAG, String.valueOf(file));

                                    for(int j = 0; j< file.size(); j++)
                                    {
                                        fileNotifSentence = file.get(j).get("notifEmail")+ ", " + fileNotifSentence;
                                    }

                                }

                                if(fileNotifSentence.contains("null"))
                                {
                                    fileNotifSentence = fileNotifSentence.replace(", null", "");
                                    notifStatus.setText(fileNotifSentence);
                                }
                                else
                                {
                                    notifStatus.setText(fileNotifSentence);
                                }
                            }
                        }
                        else if(notif[1].equals("Computer Science") ||
                                notif[1].equals("Information Technology") ||
                                notif[1].equals("Information Systems"))
                        {
                            String type = "Faculty";
                            file = retrieveData.notifyListForward(emailDeptLink, notif[1], type);
                            notifSentence = file.get(0).get("notifEmail") + " " + getString(R.string.notif_inc_forward) + " " + title;

                            id = retrieveData.notifyId(notifyIdLink, notifSentence);

                            for(int i = 0; i< id.size(); i++)
                            {
                                if(id.get(i).get("id") != null)
                                {
                                    notifSentId.add(id.get(i).get("id"));
                                }
                            }

                            if(notifSentId.size() == 1)
                            {
                                fileNotifId = notifSentId.get(0);

                                seenStatus = new SeenStatus();
                                file = seenStatus.getNotif(seenLink, fileNotifId);

                                for(int i = 0; i < file.size(); i++)
                                {
                                    String emailDept = seenStatus.getDept(getDeptLink, file.get(i).get("notifEmail"));
                                    Log.e(TAG, emailDept);
                                    if(emailDept.equals(notif[1]))
                                    {
                                        fileNotifSentence = file.get(i).get("notifEmail")+ ", " + fileNotifSentence;
                                    }
                                }

                                if(fileNotifSentence == null)
                                {
                                    notifStatus.setText("");
                                }
                                else if(fileNotifSentence.contains("null"))
                                {
                                    fileNotifSentence = fileNotifSentence.replace(", null", "");
                                    notifStatus.setText(fileNotifSentence);
                                }
                                else
                                {
                                    notifStatus.setText(fileNotifSentence);
                                }
                            }
                            else if (notifSentId.size() > 1)
                            {
                                for(int i = 1; i < notifSentId.size(); i++)
                                {
                                    seenStatus = new SeenStatus();
                                    file = seenStatus.getNotif(seenLink, String.valueOf(notifSentId.get(i)));
                                    Log.e(TAG, String.valueOf(file));

                                    for(int j = 0; j< file.size(); j++)
                                    {
                                        fileNotifSentence = file.get(j).get("notifEmail")+ ", " + fileNotifSentence;
                                    }

                                }

                                if(fileNotifSentence == null)
                                {
                                    notifStatus.setText("");
                                }
                                else if(fileNotifSentence.contains("null"))
                                {
                                    fileNotifSentence = fileNotifSentence.replace(", null", "");
                                    notifStatus.setText(fileNotifSentence);
                                }
                                else
                                {
                                    notifStatus.setText(fileNotifSentence);
                                }
                            }
                        }
                        else if(notif[1].equals("IICS"))
                        {
//                            String type = "Staff";
//                            file = retrieveData.notifyListForward(emailDeptLink, notif[1], type);
//                            notifSentence = file.get(0).get("notifEmail") + " " + getString(R.string.notif_inc_forward) + " " + title;
                            notifSentence = filecreatedBy + " " + getString(R.string.notif_inc_forward) + " " + title;
                            id = retrieveData.notifyId(notifyIdLink, notifSentence);

                            for(int i = 0; i< id.size(); i++)
                            {
                                if(id.get(i).get("id") != null)
                                {
                                    notifSentId.add(id.get(i).get("id"));
                                }
                            }

                            if(notifSentId.size() == 1)
                            {
                                fileNotifId = notifSentId.get(0);

                                seenStatus = new SeenStatus();
                                file = seenStatus.getNotif(seenLink, fileNotifId);

                                for(int i = 0; i < file.size(); i++)
                                {
                                    fileNotifSentence = file.get(i).get("notifEmail")+ ", " + fileNotifSentence;
                                }

                                if(fileNotifSentence.contains("null"))
                                {
                                    fileNotifSentence = fileNotifSentence.replace(", null", "");
                                    notifStatus.setText(fileNotifSentence);
                                }
                                else
                                {
                                    notifStatus.setText(fileNotifSentence);
                                }
                            }
                            else if (notifSentId.size() > 1)
                            {
                                for(int i = 1; i < notifSentId.size(); i++)
                                {
                                    seenStatus = new SeenStatus();
                                    file = seenStatus.getNotif(seenLink, String.valueOf(notifSentId.get(i)));
                                    Log.e(TAG, String.valueOf(file));

                                    for(int j = 0; j< file.size(); j++)
                                    {
                                        fileNotifSentence = file.get(j).get("notifEmail")+ ", " + fileNotifSentence;
                                    }

                                }

                                if(fileNotifSentence == null)
                                {
                                    notifStatus.setText("");
                                }
                                else if(fileNotifSentence.contains("null"))
                                {
                                    fileNotifSentence = fileNotifSentence.replace(", null", "");
                                    notifStatus.setText(fileNotifSentence);
                                }
                                else
                                {
                                    notifStatus.setText(fileNotifSentence);
                                }
                            }
                        }
                    }
                    else if(filestatus.equals("Received"))
                    {
                        notifSentence = filecreatedBy + " " + getString(R.string.notif_inc_upload) + " " + title;
                        id = retrieveData.notifyId(notifyIdLink, notifSentence);
                        Log.e(TAG, String.valueOf(id));
                        for(int i = 0; i< id.size(); i++)
                        {
                            if(id.get(i).get("id") != null)
                            {
                                notifSentId.add(id.get(i).get("id"));
                            }
                        }

                        if(notifSentId.size() == 1)
                        {
                            fileNotifId = notifSentId.get(0);

                            seenStatus = new SeenStatus();
                            file = seenStatus.getNotif(seenLink, fileNotifId);

                            for(int i = 0; i < file.size(); i++)
                            {
                                fileNotifSentence = file.get(i).get("notifEmail")+ ", " + fileNotifSentence;
                            }

                            if(fileNotifSentence == null)
                            {
                                notifStatus.setText("");
                            }
                            else if(fileNotifSentence.contains("null"))
                            {
                                fileNotifSentence = fileNotifSentence.replace(", null", "");
                                notifStatus.setText(fileNotifSentence);
                            }
                            else
                            {
                                notifStatus.setText(fileNotifSentence);
                            }
                        }
                        else if (notifSentId.size() > 1)
                        {
                            for(int i = 1; i < notifSentId.size(); i++)
                            {
                                seenStatus = new SeenStatus();
                                file = seenStatus.getNotif(seenLink, String.valueOf(notifSentId.get(i)));
                                Log.e(TAG, String.valueOf(file));

                                for(int j = 0; j< file.size(); j++)
                                {
                                    fileNotifSentence = file.get(j).get("notifEmail")+ ", " + fileNotifSentence;
                                }

                            }

                            if(fileNotifSentence == null)
                            {
                                notifStatus.setText("");
                            }
                            else if(fileNotifSentence.contains("null"))
                            {
                                fileNotifSentence = fileNotifSentence.replace(", null", "");
                                notifStatus.setText(fileNotifSentence);
                            }
                            else
                            {
                                notifStatus.setText(fileNotifSentence);
                            }
                        }
                    }
                    else if(filestatus.equals("Done"))
                    {
                        if(fileNotifSentence == null)
                        {
                            notifStatus.setText("");
                            notifStatusHolder.setText("");
                        }
                        else if(fileNotifSentence.contains("null"))
                        {
                            fileNotifSentence = fileNotifSentence.replace(", null", "");
                            notifStatus.setText(fileNotifSentence);
                        }
                        else
                        {
                            notifStatus.setText(fileNotifSentence);
                        }
                    }


                }
                else
                {
                    seenStatus = new SeenStatus();
                    file = seenStatus.getNotif(seenLink, fileNotifId);

                    if(file.size() > 1)
                    {
                        for(int i = 0; i < file.size(); i++)
                        {
                            fileNotifSentence = file.get(i).get("notifEmail")+ ", " + fileNotifSentence;
                        }
                    }
                    else
                    {
                        for(int i = 0; i < file.size(); i++)
                        {
                            fileNotifSentence = file.get(i).get("notifEmail")+ ", " + fileNotifSentence;
                        }
                    }

                    if(fileNotifSentence == null)
                    {
                        notifStatus.setText("");
                    }
                    else if(fileNotifSentence.contains("null"))
                    {
                        fileNotifSentence = fileNotifSentence.replace(", null", "");
                        notifStatus.setText(fileNotifSentence);
                    }
                    else
                    {
                        notifStatus.setText(fileNotifSentence);
                    }
                }

                Log.e(TAG, filecreatedBy);
                Log.e(TAG, title);

            }
            else
            {
                retrieveData = new RetrieveData(getApplicationContext());
                filePojo = retrieveData.getDocumentInfo(link, logEmail, filetype, fileId, fileThread, fileFolderId);

//                Log.e(TAG, filePojo.getDescription());
//                Log.e(TAG, filePojo.getDepartment());

                filedesc = filePojo.getDescription();
                filedept = filePojo.getDepartment();
            }
        }
        else if(filetype.equals("Outgoing"))
        {
            if(fileThread != null)
            {
                retrieveData = new RetrieveData(getApplicationContext());
                filePojo = retrieveData.getDocumentInfo(link, logEmail, filetype, fileId, fileThread, fileFolderId);

//                Log.e(TAG, filePojo.getDescription());
//                Log.e(TAG, filePojo.getDepartment());

                filedesc = filePojo.getDescription();
                filedept = filePojo.getDepartment();
            }
            else
            {
                retrieveData = new RetrieveData(getApplicationContext());
                filePojo = retrieveData.getDocumentInfo(link, logEmail, filetype, fileId, fileThread, fileFolderId);

//                Log.e(TAG, filePojo.getDescription());
//                Log.e(TAG, filePojo.getDepartment());

                filedesc = filePojo.getDescription();
                filedept = filePojo.getDepartment();
            }
        }



        fileReferenceNo.setText(fileref);
        fileSrcRecipient.setText(filesourceRecipient);
        fileCreatedBy.setText(filecreatedBy);
        fileDesc.setText(filedesc);
        fileFileName.setText(filefileName);
        fileTimeCreated.setText(filetimeCreated);
        fileType.setText(filetype);
        fileCategory.setText(filecategory);
        fileStatus.setText(filestatus);
        fileActionRequired.setText(fileactionReq);




        if(filetype.equals("Incoming")){
            fileRefNoUploadBy.setText("Reference Number");
            fileFromTo.setText("From");
            fileDescStatus.setText("Status");
            fileTimeRecTimeSent.setText("Time received");
            fileDesc.setText("");

            if(fileactionDue == null || fileactionDue.equals("null") || fileactionDue.equals("NULL") || fileactionDue.isEmpty())
            {
                fileActionDue.setText("None");
            }
            else
            {
                fileActionDue.setText(fileactionDue);
            }


        }
        else if(filetype.equals("Outgoing"))
        {
            fileRefNoUploadBy.setText("Upload by");
            fileFromTo.setText("To");
            fileDescStatus.setText("Description");
            fileTimeRecTimeSent.setText("Time sent");
            fileActionDueHolder.setText("");
            fileActionRequiredHolder.setText("");

        }
        else if(filetype.equals("Personal"))
        {
            fileRefNoUploadBy.setText("Upload by");
//            fileCreatedBy.setText("");
            fileFromTo.setText("Email");
            fileSrcRecipient.setText(logEmail);
            fileDescStatus.setText("Description");
            fileTimeRecTimeSent.setText("Time uploaded");
            fileActionDueHolder.setText("");
            fileActionRequiredHolder.setText("");
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



}

package com.iicsdms.tris.iicsdms2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tris on 03/03/2018.
 */

public class DocumentOnClickDialogFragment extends AppCompatDialogFragment{

    static final String TAG = "test";
    SharedPreferences sharedPreferences;
    Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        final String logEmail = sharedPreferences.getString("logEmail", TAG);

        final String name = getArguments().getString("FilePojoTitle");

        context = getActivity();

        final String[] forwardId = new String[1];
        final String[] choice = new String[1];
        final String[] threadNum = new String[1];
        final String[] notifId = new String[1];

        final String link = getString(R.string.email_dept_url);
        final String forwardLink = getString(R.string.forward_doc_url);
        final String forwardIdLink = getString(R.string.forward_doc_id_url);
        final String forwardUpdateLink = getString(R.string.forward_doc_update_url);
        final String threadNumLink = getString(R.string.thread_num_url);
        final String updateThreadLink = getString(R.string.update_thread_num_url);
        final String notifForward = getString(R.string.notif_forward_url);
        final String notifyListLink = getString(R.string.notif_list_url);
        final String notifyList2Link = getString(R.string.notif_list2_url);
        final String notifyIdLink = getString(R.string.notif_id_url);
        final String notifyList3Link = getString(R.string.notif_list3_url);

        final String forwardWord = getString(R.string.forward_from);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        final RetrieveData retrieveData = new RetrieveData();

        final String logDept = retrieveData.getDept(link, logEmail)[0];
        final String logRole = retrieveData.getDept(link, logEmail)[1];
        final String logName = retrieveData.getDept(link, logEmail)[2];

        CharSequence [] sequence;

        if(logRole.equals("Faculty") || logRole.equals("Staff"))
        {
            sequence = new CharSequence[]{"Read","Info"};
        }
        else
        {

            sequence = new CharSequence[]{"Read","Info", "Forward"};
        }

        builder.setTitle(name)
                .setItems(sequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                    switch (which){

                        case 0:
                            Intent intentRead = new Intent(getContext(), ViewFileActivity.class);

                            String fileFileNameRead = getArguments().getString("FilePojoFileName");
                            String fileThreadRead = getArguments().getString("FilePojoThread");
                            String fileTypeRead = getArguments().getString("FilePojoType");
                            String fileDocIdRead = getArguments().getString("FilePojoDocId");
                            String table = getArguments().getString("table");

                            intentRead.putExtra("fileFileName", fileFileNameRead);
                            intentRead.putExtra("fileThread", fileThreadRead);
                            intentRead.putExtra("fileType", fileTypeRead);
                            intentRead.putExtra("fileDocId", fileDocIdRead);
                            intentRead.putExtra("table", table);
                            intentRead.putExtra("logEmail", logEmail);

                            startActivity(intentRead);
                            break;


                        case 1:
                            String fileType = getArguments().getString("FilePojoType");

                            String fileCreatedBy = getArguments().getString("FilePojoCreatedBy");

                            String fileTitle = getArguments().getString("FilePojoTitle");
                            String fileCategory = getArguments().getString("FilePojoCategory");
                            String fileTimeCreated = getArguments().getString("FilePojoTimeCreated");
                            String fileFileName = getArguments().getString("FilePojoFileName");
                            String fileId = getArguments().getString("FilePojoDocId");

                            String fileNotifId = getArguments().getString("FilePojoNotifId");

                            String fileThread = getArguments().getString("FilePojoThread");
                            if(fileType.equals("Personal"))
                            {
                                String fileDesc = getArguments().getString("FilePojoDescription");

                                Intent intentInfo = new Intent(getContext(), ViewFileInfoActivity.class);
                                intentInfo.putExtra("fileTitle", fileTitle);

                                intentInfo.putExtra("fileCreatedBy", fileCreatedBy);

                                intentInfo.putExtra("fileCategory", fileCategory);
                                intentInfo.putExtra("fileFileName", fileFileName);
                                intentInfo.putExtra("fileTimeCreated", fileTimeCreated);
                                intentInfo.putExtra("fileType", fileType);
                                intentInfo.putExtra("fileId", fileId);
                                intentInfo.putExtra("logEmail", logEmail);
                                intentInfo.putExtra("fileDescription", fileDesc);


                                startActivity(intentInfo);
                            }
                            else if(fileType.equals("Incoming"))
                            {

                                if(fileThread != null)
                                {

                                    String fileSourceRecipient = getArguments().getString("FilePojoSourceRecipient");
                                    String fileStatus = getArguments().getString("FilePojoStatus");
                                    String fileActionReq = getArguments().getString("FilePojoActionReq");
                                    String fileActionDue = getArguments().getString("FilePojoActionDue");
                                    String fileReferenceNum = getArguments().getString("FilePojoReferenceNumber");


                                    Intent intentInfo = new Intent(getContext(), ViewFileInfoActivity.class);
                                    intentInfo.putExtra("fileTitle", fileTitle);


                                    intentInfo.putExtra("fileCategory", fileCategory);
                                    intentInfo.putExtra("fileFileName", fileFileName);
                                    intentInfo.putExtra("fileTimeCreated", fileTimeCreated);
                                    intentInfo.putExtra("fileSourceRecipient", fileSourceRecipient);
                                    intentInfo.putExtra("fileActionReq", fileActionReq);
                                    intentInfo.putExtra("fileActionDue", fileActionDue);
                                    intentInfo.putExtra("fileStatus", fileStatus);
                                    intentInfo.putExtra("fileTimeCreated", fileTimeCreated);
                                    intentInfo.putExtra("fileType", fileType);
                                    intentInfo.putExtra("fileThread", fileThread);
                                    intentInfo.putExtra("fileId", fileId);
                                    intentInfo.putExtra("fileReferenceNum", fileReferenceNum);
                                    intentInfo.putExtra("logEmail", logEmail);
                                    intentInfo.putExtra("fileNotifId", fileNotifId);
                                    intentInfo.putExtra("fileCreatedBy", fileCreatedBy);
                                    intentInfo.putExtra("logDept", logDept);
                                    startActivity(intentInfo);
                                }
                                else
                                {
                                    String fileEmail = getArguments().getString("FilePojoEmail");
                                    String fileAcadYear = getArguments().getString("FilePojoAcadYear");
                                    String fileFolderId = getArguments().getString("FilePojoFolderId");



                                    Intent intentInfo = new Intent(getContext(), ViewFileInfoActivity.class);
                                    intentInfo.putExtra("fileTitle", fileTitle);
                                    intentInfo.putExtra("fileCategory", fileCategory);
                                    intentInfo.putExtra("fileCreatedBy", fileCreatedBy);
                                    intentInfo.putExtra("fileEmail", fileEmail);
                                    intentInfo.putExtra("fileTimeCreated", fileTimeCreated);
                                    intentInfo.putExtra("fileAcadYear", fileAcadYear);
                                    intentInfo.putExtra("fileFileName", fileFileName);
                                    intentInfo.putExtra("fileType", fileType);
                                    intentInfo.putExtra("fileFolderId", fileFolderId);
                                    intentInfo.putExtra("fileId", fileId);


                                    intentInfo.putExtra("logEmail", logEmail);
                                    startActivity(intentInfo);
                                }
                            }
                            else if(fileType.equals("Outgoing"))
                            {

                                if(fileThread != null)
                                {

                                    String fileSourceRecipient = getArguments().getString("FilePojoSourceRecipient");
                                    String fileDesc = getArguments().getString("FilePojoDescription");
                                    Intent intentInfo = new Intent(getContext(), ViewFileInfoActivity.class);
                                    intentInfo.putExtra("fileTitle", fileTitle);

                                    intentInfo.putExtra("fileCreatedBy", fileCreatedBy);

                                    intentInfo.putExtra("fileCategory", fileCategory);
                                    intentInfo.putExtra("fileSourceRecipient", fileSourceRecipient);
                                    intentInfo.putExtra("fileTimeCreated", fileTimeCreated);
                                    intentInfo.putExtra("fileFileName", fileFileName);
                                    intentInfo.putExtra("fileThread", fileThread);
                                    intentInfo.putExtra("fileType", fileType);
                                    intentInfo.putExtra("fileId", fileId);
                                    intentInfo.putExtra("fileDescription", fileDesc);

                                    intentInfo.putExtra("logEmail", logEmail);
                                    startActivity(intentInfo);

                                }
                                else
                                {

                                    String fileEmail = getArguments().getString("FilePojoEmail");
                                    String fileAcadYear = getArguments().getString("FilePojoAcadYear");
                                    String fileFolderId = getArguments().getString("FilePojoFolderId");


                                    Intent intentInfo = new Intent(getContext(), ViewFileInfoActivity.class);
                                    intentInfo.putExtra("fileTitle", fileTitle);
                                    intentInfo.putExtra("fileCategory", fileCategory);
                                    intentInfo.putExtra("fileCreatedBy", fileCreatedBy);
                                    intentInfo.putExtra("fileEmail", fileEmail);
                                    intentInfo.putExtra("fileTimeCreated", fileTimeCreated);
                                    intentInfo.putExtra("fileAcadYear", fileAcadYear);
                                    intentInfo.putExtra("fileFileName", fileFileName);
                                    intentInfo.putExtra("fileType", fileType);
                                    intentInfo.putExtra("fileFolderId", fileFolderId);
                                    intentInfo.putExtra("fileId", fileId);

                                    intentInfo.putExtra("logEmail", logEmail);
                                    startActivity(intentInfo);
                                }
                            }
                            break;

                        case 2:
                            String fileTypeForward = getArguments().getString("FilePojoType");
                            final String fileThreadForward = getArguments().getString("FilePojoThread");
                            final String fileIdForward = getArguments().getString("FilePojoDocId");
                            final String fileTitleForward = getArguments().getString("FilePojoTitle");

                            final String fileReferenceNumForward = getArguments().getString("FilePojoReferenceNumber");

                            if(fileTypeForward.equals("Incoming"))
                            {
                                if(fileThreadForward != null)
                                {


                                    android.app.AlertDialog alertDialog;
                                    android.app.AlertDialog.Builder alertBuilder;


                                    final CharSequence [] sequences;

                                    if(logDept.equals("IICS"))
                                    {
                                        if(logRole.equals("Staff"))
                                        {
                                            Toast.makeText(context, "You are not allowed to forward this document.", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {

                                            sequences = new CharSequence[]{"All", "Computer Science", "Information Technology", "Information Systems"};

                                            alertBuilder = new android.app.AlertDialog.Builder(getContext());

                                            alertBuilder.setTitle(name);

                                            alertBuilder.setItems(sequences, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which)
                                                    {
                                                        case 0:
                                                            choice[0] = "All";
                                                            break;
                                                        case 1:
                                                            choice[0] = "Computer Science";
                                                            break;

                                                        case 2:
                                                            choice[0] = "Information Technology";
                                                            break;

                                                        case 3:
                                                            choice[0] = "Information Systems";
                                                            break;
                                                    }

                                                    List<Map<String,String>> file = new ArrayList<Map<String, String>>();

                                                    RetrieveData retrieveData1 = new RetrieveData();

                                                    String page = "Incoming Documents Page";
                                                    String notifDesc = logName.concat(" ").concat(context.getString(R.string.notif_inc_forward))
                                                            .concat(" ").concat(fileTitleForward);

                                                    retrieveData1.notifyForwardedDocument(notifForward, page, notifDesc);

                                                    notifId[0] = retrieveData1.notifId(notifyIdLink, notifDesc);

                                                    if(choice[0].equals("All"))
                                                    {
                                                        List<String> list = new ArrayList<String>();
                                                        list.add("Computer Science");
                                                        list.add("Information Technology");
                                                        list.add("Information Systems");

                                                        for(int i = 0; i < list.size(); i++)
                                                        {
                                                            retrieveData1.forwardDocument(forwardLink, fileIdForward, fileReferenceNumForward);
                                                            forwardId[0] = retrieveData1.getForwardId(forwardIdLink, fileReferenceNumForward);

                                                            threadNum[0] = String.valueOf(retrieveData1.getThreadNumber(threadNumLink));

                                                            retrieveData1.forwardDocumentUpdate(forwardUpdateLink, fileIdForward,
                                                                    fileReferenceNumForward, fileThreadForward, logDept,
                                                                    forwardWord.concat(" ").concat(choice[0]));

                                                            retrieveData1.forwardDocumentUpdate(forwardUpdateLink, forwardId[0],
                                                                    fileReferenceNumForward, threadNum[0], list.get(i),
                                                                    forwardWord.concat(" ").concat(logDept));

                                                            retrieveData1.updateThreadNumber(updateThreadLink, threadNum[0]);

                                                            file = retrieveData1.notifyListForward(notifyListLink, list.get(i));

                                                        }

                                                        for(int j = 0; j < file.size(); j++)
                                                        {
                                                            String flag = "Unread";

                                                            retrieveData1.notifyList(notifyList2Link, notifId[0], file.get(j).get("notifEmail"), flag);
                                                        }

                                                        Toast.makeText(context, "You have forwarded this document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }
                                                    else if(choice[0] != null)
                                                    {
                                                        retrieveData1.forwardDocument(forwardLink, fileIdForward, fileReferenceNumForward);
                                                        forwardId[0] = retrieveData1.getForwardId(forwardIdLink, fileReferenceNumForward);

                                                        threadNum[0] = String.valueOf(retrieveData1.getThreadNumber(threadNumLink));

                                                        retrieveData1.forwardDocumentUpdate(forwardUpdateLink, fileIdForward,
                                                                fileReferenceNumForward, fileThreadForward, logDept,
                                                                forwardWord.concat(" ").concat(choice[0]));

                                                        retrieveData1.forwardDocumentUpdate(forwardUpdateLink, forwardId[0],
                                                                fileReferenceNumForward, threadNum[0], choice[0],
                                                                forwardWord.concat(" ").concat(logDept));

                                                        retrieveData1.updateThreadNumber(updateThreadLink, threadNum[0]);

                                                        file = retrieveData1.notifyListForward(notifyListLink, choice[0]);


                                                        for(int i = 0; i < file.size(); i++)
                                                        {
                                                            String flag = "Unread";

                                                            retrieveData1.notifyList(notifyList2Link, notifId[0], file.get(i).get("notifEmail"), flag);
                                                        }

                                                        Toast.makeText(context, "You have forwarded this document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(context, "Failed to forward document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }


                                                }
                                            });
                                            alertDialog = alertBuilder.create();
                                            alertDialog.show();
                                        }

                                    }
                                    else if(logDept.equals("Computer Science"))
                                    {
                                        if(logRole.equals("Faculty"))
                                        {
                                            Toast.makeText(context, "You are not allowed to forward this document.", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            sequences = new CharSequence[]{"All", "Computer Science (Faculty)", "Information Technology", "Information Systems"};

                                            alertBuilder = new android.app.AlertDialog.Builder(getContext());

                                            alertBuilder.setTitle(name);

                                            alertBuilder.setItems(sequences, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which)
                                                    {
                                                        case 0:
                                                            choice[0] = "All";
                                                            break;

                                                        case 1:
                                                            choice[0] = "Computer Science";
                                                            break;

                                                        case 2:
                                                            choice[0] = "Information Technology";
                                                            break;

                                                        case 3:
                                                            choice[0] = "Information Systems";
                                                            break;
                                                    }

                                                    List<Map<String,String>> file = new ArrayList<Map<String, String>>();

                                                    RetrieveData retrieveData1 = new RetrieveData();

                                                    String page = "Incoming Documents Page";
                                                    String notifDesc = logName.concat(" ").concat(context.getString(R.string.notif_inc_forward))
                                                            .concat(" ").concat(fileTitleForward);

                                                    retrieveData1.notifyForwardedDocument(notifForward, page, notifDesc);

                                                    notifId[0] = retrieveData1.notifId(notifyIdLink, notifDesc);

                                                    if(choice[0].equals("All"))
                                                    {
                                                        List<String> list = new ArrayList<String>();
                                                        list.add("Computer Science");
                                                        list.add("Information Technology");
                                                        list.add("Information Systems");

                                                        for(int i = 0; i < list.size(); i++)
                                                        {
                                                            retrieveData1.forwardDocument(forwardLink, fileIdForward, fileReferenceNumForward);
                                                            forwardId[0] = retrieveData1.getForwardId(forwardIdLink, fileReferenceNumForward);

                                                            threadNum[0] = String.valueOf(retrieveData1.getThreadNumber(threadNumLink));

                                                            retrieveData1.forwardDocumentUpdate(forwardUpdateLink, fileIdForward,
                                                                    fileReferenceNumForward, fileThreadForward, logDept,
                                                                    forwardWord.concat(" ").concat(choice[0]));

                                                            retrieveData1.forwardDocumentUpdate(forwardUpdateLink, forwardId[0],
                                                                    fileReferenceNumForward, threadNum[0], list.get(i),
                                                                    forwardWord.concat(" ").concat(logDept));

                                                            retrieveData1.updateThreadNumber(updateThreadLink, threadNum[0]);

                                                            if(logDept.equals(list.get(i)))
                                                            {
                                                                file = retrieveData1.notifyListForward(notifyListLink, list.get(i));
                                                            }
                                                            else
                                                            {
                                                                file = retrieveData1.notifyListForward(notifyList3Link, list.get(i));
                                                            }

                                                        }

                                                        for(int j = 0; j < file.size(); j++)
                                                        {
                                                            String flag = "Unread";

                                                            retrieveData1.notifyList(notifyList2Link, notifId[0], file.get(j).get("notifEmail"), flag);
                                                        }

                                                        Toast.makeText(context, "You have forwarded this document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }
                                                    else if(choice[0] != null)
                                                    {
                                                        retrieveData1.forwardDocument(forwardLink, fileIdForward, fileReferenceNumForward);
                                                        forwardId[0] = retrieveData1.getForwardId(forwardIdLink, fileReferenceNumForward);

                                                        threadNum[0] = String.valueOf(retrieveData1.getThreadNumber(threadNumLink));

                                                        retrieveData1.forwardDocumentUpdate(forwardUpdateLink, fileIdForward,
                                                                fileReferenceNumForward, fileThreadForward, logDept,
                                                                forwardWord.concat(" ").concat(choice[0]));

                                                        retrieveData1.forwardDocumentUpdate(forwardUpdateLink, forwardId[0],
                                                                fileReferenceNumForward, threadNum[0], choice[0],
                                                                forwardWord.concat(" ").concat(logDept));

                                                        retrieveData1.updateThreadNumber(updateThreadLink, threadNum[0]);

                                                        if(logDept.equals(choice[0]))
                                                        {
                                                            file = retrieveData1.notifyListForward(notifyListLink, choice[0]);
                                                        }
                                                        else
                                                        {
                                                            file = retrieveData1.notifyListForward(notifyList3Link, choice[0]);
                                                        }



                                                        for(int i = 0; i < file.size(); i++)
                                                        {
                                                            String flag = "Unread";

                                                            retrieveData1.notifyList(notifyList2Link, notifId[0], file.get(i).get("notifEmail"), flag);
                                                        }

                                                        Toast.makeText(context, "You have forwarded this document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(context, "Failed to forward document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });

                                            alertDialog = alertBuilder.create();
                                            alertDialog.show();
                                        }
                                    }
                                    else if(logDept.equals("Information Technology"))
                                    {
                                        if(logRole.equals("Faculty"))
                                        {
                                            Toast.makeText(context, "You are not allowed to forward this document.", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {

                                            sequences = new CharSequence[]{"All", "Computer Science",
                                                    "Information Technology (Faculty)", "Information Systems"};

                                            alertBuilder = new android.app.AlertDialog.Builder(getContext());

                                            alertBuilder.setTitle(name);

                                            alertBuilder.setItems(sequences, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which)
                                                    {
                                                        case 0:
                                                            choice[0] = "All";
                                                            break;

                                                        case 1:
                                                            choice[0] = "Computer Science";
                                                            break;

                                                        case 2:
                                                            choice[0] = "Information Technology";
                                                            break;

                                                        case 3:
                                                            choice[0] = "Information Systems";
                                                            break;
                                                    }

                                                    List<Map<String,String>> file = new ArrayList<Map<String, String>>();

                                                    RetrieveData retrieveData1 = new RetrieveData();

                                                    String page = "Incoming Documents Page";
                                                    String notifDesc = logName.concat(" ").concat(context.getString(R.string.notif_inc_forward))
                                                            .concat(" ").concat(fileTitleForward);

                                                    retrieveData1.notifyForwardedDocument(notifForward, page, notifDesc);

                                                    notifId[0] = retrieveData1.notifId(notifyIdLink, notifDesc);

                                                    if(choice[0].equals("All"))
                                                    {
                                                        List<String> list = new ArrayList<String>();
                                                        list.add("Computer Science");
                                                        list.add("Information Technology");
                                                        list.add("Information Systems");

                                                        for(int i = 0; i < list.size(); i++)
                                                        {
                                                            retrieveData1.forwardDocument(forwardLink, fileIdForward, fileReferenceNumForward);
                                                            forwardId[0] = retrieveData1.getForwardId(forwardIdLink, fileReferenceNumForward);

                                                            threadNum[0] = String.valueOf(retrieveData1.getThreadNumber(threadNumLink));

                                                            retrieveData1.forwardDocumentUpdate(forwardUpdateLink, fileIdForward,
                                                                    fileReferenceNumForward, fileThreadForward, logDept,
                                                                    forwardWord.concat(" ").concat(choice[0]));

                                                            retrieveData1.forwardDocumentUpdate(forwardUpdateLink, forwardId[0],
                                                                    fileReferenceNumForward, threadNum[0], list.get(i),
                                                                    forwardWord.concat(" ").concat(logDept));

                                                            retrieveData1.updateThreadNumber(updateThreadLink, threadNum[0]);

                                                            if(logDept.equals(list.get(i)))
                                                            {
                                                                file = retrieveData1.notifyListForward(notifyListLink, list.get(i));
                                                            }
                                                            else
                                                            {
                                                                file = retrieveData1.notifyListForward(notifyList3Link, list.get(i));
                                                            }

                                                        }

                                                        for(int j = 0; j < file.size(); j++)
                                                        {
                                                            String flag = "Unread";

                                                            retrieveData1.notifyList(notifyList2Link, notifId[0], file.get(j).get("notifEmail"), flag);
                                                        }

                                                        Toast.makeText(context, "You have forwarded this document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }
                                                    else if(choice[0] != null)
                                                    {
                                                        retrieveData1.forwardDocument(forwardLink, fileIdForward, fileReferenceNumForward);
                                                        forwardId[0] = retrieveData1.getForwardId(forwardIdLink, fileReferenceNumForward);

                                                        threadNum[0] = String.valueOf(retrieveData1.getThreadNumber(threadNumLink));

                                                        retrieveData1.forwardDocumentUpdate(forwardUpdateLink, fileIdForward,
                                                                fileReferenceNumForward, fileThreadForward, logDept,
                                                                forwardWord.concat(" ").concat(choice[0]));

                                                        retrieveData1.forwardDocumentUpdate(forwardUpdateLink, forwardId[0],
                                                                fileReferenceNumForward, threadNum[0], choice[0],
                                                                forwardWord.concat(" ").concat(logDept));

                                                        retrieveData1.updateThreadNumber(updateThreadLink, threadNum[0]);

                                                        if(logDept.equals(choice[0]))
                                                        {
                                                            file = retrieveData1.notifyListForward(notifyListLink, choice[0]);
                                                        }
                                                        else
                                                        {
                                                            file = retrieveData1.notifyListForward(notifyList3Link, choice[0]);
                                                        }



                                                        for(int i = 0; i < file.size(); i++)
                                                        {
                                                            String flag = "Unread";

                                                            retrieveData1.notifyList(notifyList2Link, notifId[0], file.get(i).get("notifEmail"), flag);
                                                        }

                                                        Toast.makeText(context, "You have forwarded this document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(context, "Failed to forward document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });

                                            alertDialog = alertBuilder.create();
                                            alertDialog.show();
                                        }
                                    }
                                    else if(logDept.equals("Information Systems"))
                                    {
                                        if(logRole.equals("Faculty"))
                                        {
                                            Toast.makeText(context, "You are not allowed to forward this document.", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            sequences = new CharSequence[]{"All", "Computer Science",
                                                    "Information Technology", "Information Systems (Faculty)"};

                                            alertBuilder = new android.app.AlertDialog.Builder(getContext());

                                            alertBuilder.setTitle(name);

                                            alertBuilder.setItems(sequences, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which)
                                                    {
                                                        case 0:
                                                            choice[0] = "All";
                                                            break;

                                                        case 1:
                                                            choice[0] = "Computer Science";
                                                            break;

                                                        case 2:
                                                            choice[0] = "Information Technology";
                                                            break;

                                                        case 3:
                                                            choice[0] = "Information Systems";
                                                            break;
                                                    }

                                                    List<Map<String,String>> file = new ArrayList<Map<String, String>>();

                                                    RetrieveData retrieveData1 = new RetrieveData();

                                                    String page = "Incoming Documents Page";
                                                    String notifDesc = logName.concat(" ").concat(context.getString(R.string.notif_inc_forward))
                                                            .concat(" ").concat(fileTitleForward);

                                                    retrieveData1.notifyForwardedDocument(notifForward, page, notifDesc);

                                                    notifId[0] = retrieveData1.notifId(notifyIdLink, notifDesc);

                                                    if(choice[0].equals("All"))
                                                    {
                                                        List<String> list = new ArrayList<String>();
                                                        list.add("Computer Science");
                                                        list.add("Information Technology");
                                                        list.add("Information Systems");

                                                        for(int i = 0; i < list.size(); i++)
                                                        {
                                                            retrieveData1.forwardDocument(forwardLink, fileIdForward, fileReferenceNumForward);
                                                            forwardId[0] = retrieveData1.getForwardId(forwardIdLink, fileReferenceNumForward);

                                                            threadNum[0] = String.valueOf(retrieveData1.getThreadNumber(threadNumLink));

                                                            retrieveData1.forwardDocumentUpdate(forwardUpdateLink, fileIdForward,
                                                                    fileReferenceNumForward, fileThreadForward, logDept,
                                                                    forwardWord.concat(" ").concat(choice[0]));

                                                            retrieveData1.forwardDocumentUpdate(forwardUpdateLink, forwardId[0],
                                                                    fileReferenceNumForward, threadNum[0], list.get(i),
                                                                    forwardWord.concat(" ").concat(logDept));

                                                            retrieveData1.updateThreadNumber(updateThreadLink, threadNum[0]);

                                                            if(logDept.equals(list.get(i)))
                                                            {
                                                                file = retrieveData1.notifyListForward(notifyListLink, list.get(i));
                                                            }
                                                            else
                                                            {
                                                                file = retrieveData1.notifyListForward(notifyList3Link, list.get(i));
                                                            }

                                                        }

                                                        for(int j = 0; j < file.size(); j++)
                                                        {
                                                            String flag = "Unread";

                                                            retrieveData1.notifyList(notifyList2Link, notifId[0], file.get(j).get("notifEmail"), flag);
                                                        }

                                                        Toast.makeText(context, "You have forwarded this document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }
                                                    else if(choice[0] != null)
                                                    {
                                                        retrieveData1.forwardDocument(forwardLink, fileIdForward, fileReferenceNumForward);
                                                        forwardId[0] = retrieveData1.getForwardId(forwardIdLink, fileReferenceNumForward);

                                                        threadNum[0] = String.valueOf(retrieveData1.getThreadNumber(threadNumLink));

                                                        retrieveData1.forwardDocumentUpdate(forwardUpdateLink, fileIdForward,
                                                                fileReferenceNumForward, fileThreadForward, logDept,
                                                                forwardWord.concat(" ").concat(choice[0]));

                                                        retrieveData1.forwardDocumentUpdate(forwardUpdateLink, forwardId[0],
                                                                fileReferenceNumForward, threadNum[0], choice[0],
                                                                forwardWord.concat(" ").concat(logDept));

                                                        retrieveData1.updateThreadNumber(updateThreadLink, threadNum[0]);


                                                        if(logDept.equals(choice[0]))
                                                        {
                                                            file = retrieveData1.notifyListForward(notifyListLink, choice[0]);
                                                        }
                                                        else
                                                        {
                                                            file = retrieveData1.notifyListForward(notifyList3Link, choice[0]);
                                                        }

                                                        for(int i = 0; i < file.size(); i++)
                                                        {
                                                            String flag = "Unread";

                                                            retrieveData1.notifyList(notifyList2Link, notifId[0], file.get(i).get("notifEmail"), flag);
                                                        }

                                                        Toast.makeText(context, "You have forwarded this document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(context, "Failed to forward document to " + choice[0] + " department.", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });

                                            alertDialog = alertBuilder.create();
                                            alertDialog.show();
                                        }
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "This document cannot be forwarded.", Toast.LENGTH_LONG).show();
                            }

                            break;

                    }
                    }
                })
                .setCancelable(false);
        return builder.create();
    }
}

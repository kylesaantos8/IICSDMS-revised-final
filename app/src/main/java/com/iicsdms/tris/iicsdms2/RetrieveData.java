package com.iicsdms.tris.iicsdms2;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tris on 14/03/2018.
 */

class RetrieveData {

    static final String TAG = "Test";
    private Context ctx;

    private InputStream inputStream;
    private JSONArray jsonArray;
    private List<Map<String,String>> file = new ArrayList<Map<String, String>>();
    private String line, result, forwardId, notifId;
    private int threadNumber;
    private String role[];

    RetrieveData ()
    {

    }

    RetrieveData(Context context)
    {
        ctx = context;
    }
//  -------------------------------------------- notification --------------------------------------
    List getNotif(String link, String logEmail)
    {
        NotifPojo notifPojo;

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(logEmail.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return file;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    notifPojo = new NotifPojo();
                    notifPojo.setNotifId(jsonObject.getString("id"));
                    notifPojo.setNotifPage(jsonObject.getString("page"));
                    notifPojo.setNotifDescription(jsonObject.getString("description"));
                    notifPojo.setNotifTimeStamp(jsonObject.getString("notif_timestamp"));
                    notifPojo.setNotifFlag(jsonObject.getString("flag"));

                    fileMap.put("notifId", notifPojo.getNotifId());
                    fileMap.put("notifPage", notifPojo.getNotifPage());
                    fileMap.put("notifDescription", notifPojo.getNotifDescription());
                    fileMap.put("notifTimeStamp", notifPojo.getNotifTimeStamp());
                    fileMap.put("notifFlag", notifPojo.getNotifFlag());

                    file.add(fileMap);
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

//  -------------------------------------------- document list -------------------------------------
    List getDocumentList(String link, String table, String email)
    {
        FilePojo filePojo;
        String query;
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            if(table.equals("outgoing_documents"))
            {
                query = "SELECT id, thread_number, source_recipient, title, category, time_created, type, file_name FROM " +
                        table + " WHERE department = " + "(SELECT department from accounts where email = '" +
                        email + "') ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setFileName(jsonObject.getString("file_name"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("fileName", filePojo.getFileName());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("incoming_documents"))
            {
                query = "SELECT id, thread_number, title, source_recipient, time_created, category, action_required, due_on, status, " +
                        "reference_no, file_name, type FROM " + table + " WHERE department = (SELECT department from accounts where email = '" +
                        email + "') ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setActionReq(jsonObject.getString("action_required"));
                        filePojo.setActionDue(jsonObject.getString("due_on"));
                        filePojo.setStatus(jsonObject.getString("status"));
                        filePojo.setReferenceNum(jsonObject.getString("reference_no"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setType(jsonObject.getString("type"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("actionReq", filePojo.getActionReq());
                        fileMap.put("actionDue", filePojo.getActionDue());
                        fileMap.put("status", filePojo.getStatus());
                        fileMap.put("refNum", filePojo.getReferenceNum());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("id", filePojo.getDocId());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("archived_documents"))
            {
                query = "SELECT archived_documents.id, archived_documents.folder_id, archived_documents.type, " +
                        "archived_documents.source_recipient, archived_documents.title, archived_documents.category, " +
                        "archived_documents.file_name, archived_documents.uploaded_by, archived_documents.email, " +
                        "archived_documents.upload_date, archived_documents.academic_year FROM " + table +
                        " INNER JOIN archived_folder ON archived_folder.status = 'Enabled' AND department = " +
                        "(SELECT department FROM accounts WHERE email = '" + email + "') ORDER BY upload_date DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setFolderId(jsonObject.getString("folder_id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setCreatedBy(jsonObject.getString("uploaded_by"));
                        filePojo.setEmail(jsonObject.getString("email"));
                        filePojo.setTimeCreated(jsonObject.getString("upload_date"));
                        filePojo.setAcademicYear(jsonObject.getString("academic_year"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("sourceRecipient", filePojo.getSourceRecipient());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        fileMap.put("email", filePojo.getEmail());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("academicYear", filePojo.getAcademicYear());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("folderId", filePojo.getFolderId());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("personal_documents"))
            {
                query = "SELECT id, type, title, category, file_name, time_created, created_by FROM "
                        + table + " WHERE email = '" + email + "' ORDER BY time_created DESC";
                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        file.add(fileMap);
                    }
                }
            }
            else if (table.trim().equals("all"))
            {
                query = "(SELECT type, id, thread_number, title, category, file_name, created_by, email, time_created, " +
                        "source_recipient, status " + "FROM `incoming_documents` WHERE department in (SELECT department " +
                        "from `accounts` where email = '" + email + "')) " +
                        "UNION (SELECT type, id, thread_number, title, category, file_name, created_by, email, time_created, " +
                        "source_recipient, null FROM `outgoing_documents` WHERE department in (SELECT department " +
                        "from `accounts` where email = '" + email + "')) " +
                        "UNION (SELECT type, id, null, title, category, file_name, created_by, email, time_created, null, null " +
                        "FROM `personal_documents` WHERE email = '" + email + "')  ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));
                        filePojo.setEmail(jsonObject.getString("email"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setStatus(jsonObject.getString("status"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        fileMap.put("email", filePojo.getEmail());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("status", filePojo.getStatus());

                        file.add(fileMap);
                    }
                }
            }


        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    List getDocumentList(String link, String table, String email, String statusCheck)
    {
        FilePojo filePojo;
        String query;
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            if(table.equals("outgoing_documents"))
            {
                query = "SELECT id, thread_number, source_recipient, title, category, time_created, type, file_name FROM " +
                        table + " WHERE department = " + "(SELECT department from accounts where email = '" +
                        email + "') ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setFileName(jsonObject.getString("file_name"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("fileName", filePojo.getFileName());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("incoming_documents"))
            {
                query = "SELECT id, thread_number, title, source_recipient, time_created, category, action_required, due_on, status, " +
                        "reference_no, file_name, type FROM " + table + " WHERE (department = (SELECT department from accounts where " +
                        "email = '" + email + "')) AND ((department = (SELECT department from accounts where " +
                        "email = '" + email + "') AND status = 'Forwarded: IICS') OR (department = (SELECT department from accounts " +
                        "where email = '" + email + "') AND status = '" + statusCheck + "')) ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setActionReq(jsonObject.getString("action_required"));
                        filePojo.setActionDue(jsonObject.getString("due_on"));
                        filePojo.setStatus(jsonObject.getString("status"));
                        filePojo.setReferenceNum(jsonObject.getString("reference_no"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setType(jsonObject.getString("type"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("actionReq", filePojo.getActionReq());
                        fileMap.put("actionDue", filePojo.getActionDue());
                        fileMap.put("status", filePojo.getStatus());
                        fileMap.put("refNum", filePojo.getReferenceNum());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("id", filePojo.getDocId());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("archived_documents"))
            {
                query = "SELECT archived_documents.id, archived_documents.folder_id, archived_documents.type, " +
                        "archived_documents.source_recipient, archived_documents.title, archived_documents.category, " +
                        "archived_documents.file_name, archived_documents.uploaded_by, archived_documents.email, " +
                        "archived_documents.upload_date, archived_documents.academic_year FROM " + table +
                        " INNER JOIN archived_folder ON archived_folder.status = 'Enabled' AND department = " +
                        "(SELECT department FROM accounts WHERE email = '" + email + "') ORDER BY upload_date DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setFolderId(jsonObject.getString("folder_id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setCreatedBy(jsonObject.getString("uploaded_by"));
                        filePojo.setEmail(jsonObject.getString("email"));
                        filePojo.setTimeCreated(jsonObject.getString("upload_date"));
                        filePojo.setAcademicYear(jsonObject.getString("academic_year"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("sourceRecipient", filePojo.getSourceRecipient());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        fileMap.put("email", filePojo.getEmail());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("academicYear", filePojo.getAcademicYear());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("folderId", filePojo.getFolderId());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("personal_documents"))
            {
                query = "SELECT id, type, title, category, file_name, time_created, created_by FROM "
                        + table + " WHERE email = '" + email + "' ORDER BY time_created DESC";
                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        file.add(fileMap);
                    }
                }
            }
            else if (table.trim().equals("all"))
            {
                query = "(SELECT type, id, thread_number, title, category, file_name, created_by, email, time_created, " +
                        "source_recipient, status " + "FROM `incoming_documents` WHERE (department = (SELECT department from accounts where " +
                        "email = '" + email + "')) AND ((department = (SELECT department from accounts where " +
                        "email = '" + email + "') AND status = 'Forwarded: IICS') OR (department = (SELECT department from accounts " +
                        "where email = '" + email + "') AND status = '" + statusCheck + "'))) " +
                        "UNION (SELECT type, id, thread_number, title, category, file_name, created_by, email, time_created, " +
                        "source_recipient, null FROM `outgoing_documents` WHERE department in (SELECT department " +
                        "from `accounts` where email = '" + email + "')) " +
                        "UNION (SELECT type, id, null, title, category, file_name, created_by, email, time_created, null, null " +
                        "FROM `personal_documents` WHERE email = '" + email + "')  ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));
                        filePojo.setEmail(jsonObject.getString("email"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setStatus(jsonObject.getString("status"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        fileMap.put("email", filePojo.getEmail());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("status", filePojo.getStatus());

                        file.add(fileMap);
                    }
                }
            }


        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

//  ------------------------------------------ search document -------------------------------------
    List searchDocument(String link, String table, String email, String search)
    {
        FilePojo filePojo;
        String query;
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            if(table.equals("outgoing_documents"))
            {
                query = "SELECT id, thread_number, source_recipient, title, category, time_created, type, file_name " +
                        "FROM "+ table + " WHERE department = (SELECT department from accounts where email = '" + email + "') AND " +
                        "title LIKE '%" + search + "%' ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setFileName(jsonObject.getString("file_name"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("fileName", filePojo.getFileName());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("incoming_documents"))
            {
                query = "SELECT id, thread_number, title, source_recipient, time_created, category, action_required, due_on, status, " +
                        "reference_no, file_name, type FROM "+ table + " WHERE department = (SELECT department from accounts where " +
                        "email = '" + email + "') AND title LIKE '%" + search + "%' ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setActionReq(jsonObject.getString("action_required"));
                        filePojo.setActionDue(jsonObject.getString("due_on"));
                        filePojo.setStatus(jsonObject.getString("status"));
                        filePojo.setReferenceNum(jsonObject.getString("reference_no"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setType(jsonObject.getString("type"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("actionReq", filePojo.getActionReq());
                        fileMap.put("actionDue", filePojo.getActionDue());
                        fileMap.put("status", filePojo.getStatus());
                        fileMap.put("refNum", filePojo.getReferenceNum());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("id", filePojo.getDocId());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("archived_documents"))
            {
                query = "SELECT archived_documents.id, archived_documents.folder_id, archived_documents.type, " +
                        "archived_documents.source_recipient, archived_documents.title, archived_documents.category, " +
                        "archived_documents.file_name, archived_documents.uploaded_by, archived_documents.email, " +
                        "archived_documents.upload_date, archived_documents.academic_year FROM " + table + " INNER JOIN archived_folder " +
                        "ON archived_folder.status = 'Enabled' AND department = (SELECT department FROM accounts WHERE " +
                        "email = '" + email + "') AND archived_documents.title LIKE '%" + search + "%' ORDER BY upload_date DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setFolderId(jsonObject.getString("folder_id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setCreatedBy(jsonObject.getString("uploaded_by"));
                        filePojo.setEmail(jsonObject.getString("email"));
                        filePojo.setTimeCreated(jsonObject.getString("upload_date"));
                        filePojo.setAcademicYear(jsonObject.getString("academic_year"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("sourceRecipient", filePojo.getSourceRecipient());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        fileMap.put("email", filePojo.getEmail());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("academicYear", filePojo.getAcademicYear());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("folderId", filePojo.getFolderId());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("personal_documents"))
            {
                query = "SELECT id, type, title, category, file_name, time_created, created_by FROM "
                        + table + " WHERE email = '" + email + "' AND title LIKE '%" + search + "%' ORDER BY time_created DESC";
                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        file.add(fileMap);
                    }
                }
            }
            else if (table.trim().equals("all"))
            {
                query = "(SELECT type, id, thread_number, title, category, file_name, created_by, email, time_created, " +
                        "source_recipient, status FROM `incoming_documents` WHERE department in (SELECT department " +
                        "from `accounts` where email = '" + email + "') AND title LIKE '%" + search + "%') " +
                        "UNION " +
                        "(SELECT type, id, thread_number, title, category, file_name, created_by, email, time_created, " +
                        "source_recipient, null FROM `outgoing_documents` WHERE department in (SELECT department " +
                        "from `accounts` where email = '" + email + "') AND title LIKE '%" + search + "%') " +
                        "UNION " +
                        "(SELECT type, id, null, title, category, file_name, created_by, email, time_created, null, null FROM " +
                        "`personal_documents` WHERE email = '" + email + "' AND title LIKE '%" + search + "%')  ORDER BY " +
                        "time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));
                        filePojo.setEmail(jsonObject.getString("email"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setStatus(jsonObject.getString("status"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        fileMap.put("email", filePojo.getEmail());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("status", filePojo.getStatus());

                        file.add(fileMap);
                    }
                }
            }


        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    List searchDocument(String link, String table, String email, String search, String statusCheck)
    {
        FilePojo filePojo;
        String query;
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            if(table.equals("outgoing_documents"))
            {
                query = "SELECT id, thread_number, source_recipient, title, category, time_created, type, file_name " +
                        "FROM "+ table + " WHERE department = (SELECT department from accounts where email = '" + email + "') AND " +
                        "title LIKE '%" + search + "%' ORDER BY time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setFileName(jsonObject.getString("file_name"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("fileName", filePojo.getFileName());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("incoming_documents"))
            {
                query = "SELECT id, thread_number, title, source_recipient, time_created, category, action_required, due_on, status, " +
                        "reference_no, file_name, type FROM "+ table + " WHERE (department = (SELECT department from accounts where " +
                        "email = '" + email + "')) AND ((department = (SELECT department from accounts where " +
                        "email = '" + email + "') AND status = 'Forwarded: IICS') OR (department = (SELECT department from accounts " +
                        "where email = '" + email + "') AND status = '" + statusCheck + "')) AND title LIKE '%" + search+ "%' " +
                        "ORDER BY time_created DESC";


                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setActionReq(jsonObject.getString("action_required"));
                        filePojo.setActionDue(jsonObject.getString("due_on"));
                        filePojo.setStatus(jsonObject.getString("status"));
                        filePojo.setReferenceNum(jsonObject.getString("reference_no"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setType(jsonObject.getString("type"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("actionReq", filePojo.getActionReq());
                        fileMap.put("actionDue", filePojo.getActionDue());
                        fileMap.put("status", filePojo.getStatus());
                        fileMap.put("refNum", filePojo.getReferenceNum());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("id", filePojo.getDocId());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("archived_documents"))
            {
                query = "SELECT archived_documents.id, archived_documents.folder_id, archived_documents.type, " +
                        "archived_documents.source_recipient, archived_documents.title, archived_documents.category, " +
                        "archived_documents.file_name, archived_documents.uploaded_by, archived_documents.email, " +
                        "archived_documents.upload_date, archived_documents.academic_year FROM " + table + " INNER JOIN archived_folder " +
                        "ON archived_folder.status = 'Enabled' AND department = (SELECT department FROM accounts WHERE " +
                        "email = '" + email + "') AND archived_documents.title LIKE '%" + search + "%' ORDER BY upload_date DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setFolderId(jsonObject.getString("folder_id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setCreatedBy(jsonObject.getString("uploaded_by"));
                        filePojo.setEmail(jsonObject.getString("email"));
                        filePojo.setTimeCreated(jsonObject.getString("upload_date"));
                        filePojo.setAcademicYear(jsonObject.getString("academic_year"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("sourceRecipient", filePojo.getSourceRecipient());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        fileMap.put("email", filePojo.getEmail());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("academicYear", filePojo.getAcademicYear());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("folderId", filePojo.getFolderId());
                        file.add(fileMap);
                    }
                }
            }
            else if(table.equals("personal_documents"))
            {
                query = "SELECT id, type, title, category, file_name, time_created, created_by FROM "
                        + table + " WHERE email = '" + email + "' AND title LIKE '%" + search + "%' ORDER BY time_created DESC";
                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        file.add(fileMap);
                    }
                }
            }
            else if (table.trim().equals("all"))
            {
                query = "(SELECT type, id, thread_number, title, category, file_name, created_by, email, time_created, " +
                        "source_recipient, status FROM `incoming_documents` WHERE (department = (SELECT department from accounts where " +
                        "email = '" + email + "')) AND ((department = (SELECT department from accounts where " +
                        "email = '" + email + "') AND status = 'Forwarded: IICS') OR (department = (SELECT department from accounts " +
                        "where email = '" + email + "') AND status = '" + statusCheck + "')) AND title LIKE '%" + search + "%') " +
                        "UNION " +
                        "(SELECT type, id, thread_number, title, category, file_name, created_by, email, time_created, " +
                        "source_recipient, null FROM `outgoing_documents` WHERE department in (SELECT department " +
                        "from `accounts` where email = '" + email + "') AND title LIKE '%" + search + "%') " +
                        "UNION " +
                        "(SELECT type, id, null, title, category, file_name, created_by, email, time_created, null, null FROM " +
                        "`personal_documents` WHERE email = '" + email + "' AND title LIKE '%" + search + "%')  ORDER BY " +
                        "time_created DESC";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();

                result = stringBuilder.toString();

                if(result.trim().isEmpty() || result.trim().equals("No Results found") || (result == null))
                {
                    return file;
                }
                else
                {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        Map<String,String> fileMap = new HashMap<String, String>(2);
                        jsonObject = jsonArray.getJSONObject(i);
                        filePojo.setDocId(jsonObject.getString("id"));
                        filePojo.setType(jsonObject.getString("type"));
                        filePojo.setThread(jsonObject.getString("thread_number"));
                        filePojo.setFileName(jsonObject.getString("file_name"));
                        filePojo.setTitle(jsonObject.getString("title"));
                        filePojo.setCategory(jsonObject.getString("category"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));
                        filePojo.setEmail(jsonObject.getString("email"));
                        filePojo.setTimeCreated(jsonObject.getString("time_created"));
                        filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                        filePojo.setStatus(jsonObject.getString("status"));

                        fileMap.put("title", filePojo.getTitle());
                        fileMap.put("category", filePojo.getCategory());
                        fileMap.put("createdBy", filePojo.getCreatedBy());
                        fileMap.put("email", filePojo.getEmail());
                        fileMap.put("timeCreated", filePojo.getTimeCreated());
                        fileMap.put("srcRecipient", filePojo.getSourceRecipient());
                        fileMap.put("fileName", filePojo.getFileName());
                        fileMap.put("thread", filePojo.getThread());
                        fileMap.put("type", filePojo.getType());
                        fileMap.put("id", filePojo.getDocId());
                        fileMap.put("status", filePojo.getStatus());

                        file.add(fileMap);
                    }
                }
            }


        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

//  --------------------------------------- view thread --------------------------------------------

    List getThread(String link, String thread)
    {
        FilePojo filePojo;

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("thread", "UTF-8")+"="+URLEncoder.encode(thread.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();


            //Log.e(test, result);

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return file;

            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    filePojo = new FilePojo();
                    filePojo.setDocId(jsonObject.getString("id"));
                    filePojo.setType(jsonObject.getString("type"));
                    filePojo.setThread(jsonObject.getString("thread_number"));
                    filePojo.setReferenceNum(jsonObject.getString("reference_no"));
                    filePojo.setSourceRecipient(jsonObject.getString("source_recipient"));
                    filePojo.setTitle(jsonObject.getString("title"));
                    filePojo.setCategory(jsonObject.getString("category"));
                    filePojo.setActionReq(jsonObject.getString("action_required"));
                    filePojo.setActionDue(jsonObject.getString("due_on"));
                    filePojo.setFileName(jsonObject.getString("file_name"));
                    filePojo.setCreatedBy(jsonObject.getString("created_by"));
                    filePojo.setEmail(jsonObject.getString("email"));
                    filePojo.setStatus(jsonObject.getString("status"));
                    filePojo.setTimeCreated(jsonObject.getString("time_created"));
                    filePojo.setDescription(jsonObject.getString("description"));

                    fileMap.put("id", filePojo.getDocId());
                    fileMap.put("type", filePojo.getType());
                    fileMap.put("thread", filePojo.getThread());
                    fileMap.put("referenceNum", filePojo.getReferenceNum());
                    fileMap.put("sourceRecipient", filePojo.getSourceRecipient());
                    fileMap.put("title", filePojo.getTitle());
                    fileMap.put("category", filePojo.getCategory());
                    fileMap.put("actionReq", filePojo.getActionReq());
                    fileMap.put("actionDue", filePojo.getActionDue());
                    fileMap.put("fileName", filePojo.getFileName());
                    fileMap.put("createdBy", filePojo.getCreatedBy());
                    fileMap.put("email", filePojo.getEmail());
                    fileMap.put("status", filePojo.getStatus());
                    fileMap.put("timeCreated", filePojo.getTimeCreated());
                    fileMap.put("desc", filePojo.getDescription());
                    fileMap.put("upBy", filePojo.getCreatedBy().concat(" (").concat(filePojo.getEmail()).concat(")"));
                    file.add(fileMap);
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    List getThreadInfo(String link, String table, String title, String fullName)
    {
        NotifPojo notifPojo;

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("title", "UTF-8")+"="+URLEncoder.encode(title.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("full_name", "UTF-8")+"="+URLEncoder.encode(fullName.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();


            //Log.e(test, result);

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return file;

            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    notifPojo = new NotifPojo();
                    notifPojo.setNotifThread(jsonObject.getString("thread_number"));
                    notifPojo.setNotifSourceRecipient(jsonObject.getString("source_recipient"));

                    fileMap.put("thread", notifPojo.getNotifThread());
                    fileMap.put("sourceRecipient", notifPojo.getNotifSourceRecipient());

                    file.add(fileMap);
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

//  ------------------------------------------------------------------------------------------------



//  ---------------------------------- forward document --------------------------------------------
    String[] getDept(String link, String email)
    {

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();


            //Log.e(test, result);

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return role;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    jsonObject = jsonArray.getJSONObject(i);

                    role = new String[]
                            {
                                    jsonObject.getString("department"),
                                    jsonObject.getString("user_type"),
                                    jsonObject.getString("full_name")
                            };

                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return role;
    }

    void forwardDocument(String link, String docId, String docRefNum)
    {
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(docId.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("ref_no", "UTF-8")+"="+URLEncoder.encode(docRefNum.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void forwardDocumentUpdate(String link, String docId, String docRefNum, String thread, String department, String status)
    {
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(docId.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("ref_no", "UTF-8")+"="+URLEncoder.encode(docRefNum.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("thread", "UTF-8")+"="+URLEncoder.encode(thread.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("department", "UTF-8")+"="+URLEncoder.encode(department.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("status", "UTF-8")+"="+URLEncoder.encode(status.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    int getThreadNumber(String link)
    {

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode("documents".trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return threadNumber;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();
                    jsonObject = jsonArray.getJSONObject(i);

                    threadNumber = jsonObject.getInt("counter");
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return ++threadNumber;
    }

    String getForwardId(String link, String referenceNum)
    {

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("ref_no", "UTF-8")+"="+URLEncoder.encode(referenceNum.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return forwardId;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();
                    jsonObject = jsonArray.getJSONObject(i);

                    forwardId = jsonObject.getString("id");
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return forwardId;
    }

    void updateThreadNumber(String link, String thread)
    {
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("thread", "UTF-8")+"="+URLEncoder.encode(thread.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode("documents".trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
//  ------------------------------------------------------------------------------------------------

//  -------------------------------- forward notification ------------------------------------------
    void notifyForwardedDocument(String link, String page, String desc)
    {
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("page", "UTF-8")+"="+URLEncoder.encode(page.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(desc.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

//  ----------------------------------- forward notif list -----------------------------------------
    List notifyListForward(String link, String dept)
    {
        NotifPojo notifPojo;

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("department", "UTF-8")+"="+URLEncoder.encode(dept.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return file;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    notifPojo = new NotifPojo();
                    notifPojo.setEmail(jsonObject.getString("email"));

                    fileMap.put("notifEmail", notifPojo.getEmail());

                    file.add(fileMap);
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    List notifyListForward(String link, String dept, String type)
    {
        NotifPojo notifPojo;

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("department", "UTF-8")+"="+URLEncoder.encode(dept.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(type.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return file;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    notifPojo = new NotifPojo();
                    notifPojo.setEmail(jsonObject.getString("full_name"));

                    fileMap.put("notifEmail", notifPojo.getEmail());

                    file.add(fileMap);
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    //  ----------------------------------- forward notif list -----------------------------------------
    String notifId(String link, String desc)
    {

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(desc.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return notifId;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    notifId = jsonObject.getString("id");
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return notifId;
    }

    List notifyId(String link, String desc)
    {

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(desc.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return file;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    notifId = jsonObject.getString("id");

                    fileMap.put("id", notifId);

                    file.add(fileMap);
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    //  -------------------------------- forward notification list --------------------------------------
    void notifyList(String link, String id, String email, String flag)
    {
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("flag", "UTF-8")+"="+URLEncoder.encode(flag.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

//  --------------------------------- profile ------------------------------------------------------

    public ProfilePojo getFileProfile(String link, String logEmail)
    {
        ProfilePojo profilePojo = new ProfilePojo();
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(logEmail.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();
            result = stringBuilder.toString();

            if(result.trim().equals("No Results found"))
            {
                return profilePojo;
            }
            else
            {

                JSONArray jsonArray = new JSONArray(result.trim());
                JSONObject jsonObject;


                for(int i = 0; i < jsonArray.length(); i++)
                {
                    jsonObject = jsonArray.getJSONObject(i);

                    profilePojo.setTitle(jsonObject.getString("title"));
                    profilePojo.setContactNumber(jsonObject.getString("contact_number"));
                    profilePojo.setFacNumber(jsonObject.getString("faculty_number"));
                    profilePojo.setName(jsonObject.getString("full_name"));
                    profilePojo.setUserType(jsonObject.getString("user_type"));
                    profilePojo.setDepartment(jsonObject.getString("department"));
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }

        return profilePojo;
    }

//  ---------------------------------------- get file ----------------------------------------------
    byte[] getFileDocumentData(String link, String type, String id, String thread)
    {
        byte[] data1 = new byte[0];
        String table, query, data = null;
        try
        {

            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            if(thread != null)
            {
                if(type.trim().equals("Incoming"))
                {
                    table = "incoming_documents";
                    query = "SELECT file_data FROM `incoming_documents` WHERE id = '" + id.trim()
                            + "' AND thread_number = '" + thread.trim() +"'";

                    data = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");
                }
                else if(type.trim().equals("Outgoing"))
                {
                    table = "outgoing_documents";
                    query = "SELECT file_data FROM `outgoing_documents` WHERE id = '" + id.trim()
                            + "' AND thread_number = '" + thread.trim() +"'";

                    data = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");
                }
            }
            else
            {
                if(type.trim().equals("Personal"))
                {
                    table = "personal_documents";
                    query = "SELECT file_data FROM `personal_documents` WHERE id = '" + id.trim() + "'";

                    data = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");
                }
                else
                {
                    table = "archived_documents";
                    query = "SELECT file_data FROM `archived_documents` WHERE id = '" + id.trim() + "'";

                    data = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query.trim(),"UTF-8");
                }
            }

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            result = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();

            //fileTest = result;
            data1 = Base64.decode(result.getBytes(), Base64.DEFAULT);

            httpURLConnection.disconnect();
        }
        catch(Exception me)
        {
            me.printStackTrace();
        }


        return data1;
    }

//  ----------------------------------------- get file info ----------------------------------------
    public FilePojo getDocumentInfo(String link, String logEmail, String type, String fileId, String thread, String folderId)
    {
        FilePojo filePojo = null;
        String query, table;
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            if(type.equals("Personal"))
            {
                query = "SELECT `description`, `created_by` FROM `personal_documents` WHERE email = '" + logEmail + "'" +
                        " AND id = '" + fileId + "'";

                String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query,"UTF-8");

                bufferedWriter.write(data1);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();
                result = stringBuilder.toString();

                if(result.trim().equals("No Results found"))
                {
                    return filePojo;
                }
                else
                {

                    JSONArray jsonArray = new JSONArray(result.trim());
                    JSONObject jsonObject;

                    filePojo = new FilePojo();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);

                        filePojo.setDescription(jsonObject.getString("description"));
                        filePojo.setCreatedBy(jsonObject.getString("created_by"));
                    }
                }
            }
            else if(type.equals("Incoming"))
            {
                if(thread != null)
                {
                    query = "SELECT `description`, `department`, `note` FROM `incoming_documents` " +
                            "WHERE id = '" + fileId + "' AND thread_number = '" + thread + "'";

                    String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query,"UTF-8");

                    bufferedWriter.write(data1);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    while((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    result = stringBuilder.toString();

                    if(result.trim().equals("No Results found"))
                    {
                        return filePojo;
                    }
                    else
                    {

                        JSONArray jsonArray = new JSONArray(result.trim());
                        JSONObject jsonObject;

                        filePojo = new FilePojo();
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            filePojo.setDescription(jsonObject.getString("description"));
                            filePojo.setDepartment(jsonObject.getString("department"));
                            filePojo.setNote(jsonObject.getString("note"));
                        }
                    }
                }
                else
                {
                    query = "SELECT `description`, `department` FROM `archived_documents` WHERE id = '" + fileId + "' AND folder_id = " +
                            "'" + folderId + "'";

                    String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query,"UTF-8");

                    bufferedWriter.write(data1);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    while((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    result = stringBuilder.toString();

                    if(result.trim().equals("No Results found"))
                    {
                        return filePojo;
                    }
                    else
                    {

                        JSONArray jsonArray = new JSONArray(result.trim());
                        JSONObject jsonObject;

                        filePojo = new FilePojo();
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            filePojo.setDescription(jsonObject.getString("description"));
                            filePojo.setDepartment(jsonObject.getString("department"));
                        }
                    }
                }
            }
            else if(type.equals("Outgoing"))
            {
                if(thread != null)
                {
                    query = "SELECT `description`, `department` FROM `outgoing_documents` " +
                                "WHERE id = '" + fileId + "' AND thread_number = '" + thread + "'";

                    String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query,"UTF-8");

                    bufferedWriter.write(data1);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    while((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    result = stringBuilder.toString();

                    if(result.trim().equals("No Results found"))
                    {
                        return filePojo;
                    }
                    else
                    {

                        JSONArray jsonArray = new JSONArray(result.trim());
                        JSONObject jsonObject;

                        filePojo = new FilePojo();
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            filePojo.setDescription(jsonObject.getString("description"));
                            filePojo.setDepartment(jsonObject.getString("department"));
                        }
                    }
                }
                else
                {
                    query = "SELECT `description`, `department` FROM `archived_documents` WHERE id = '" + fileId + "'";

                    String data1 = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query,"UTF-8");

                    bufferedWriter.write(data1);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    while((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    result = stringBuilder.toString();

                    if(result.trim().equals("No Results found"))
                    {
                        return filePojo;
                    }
                    else
                    {

                        JSONArray jsonArray = new JSONArray(result.trim());
                        JSONObject jsonObject;

                        filePojo = new FilePojo();
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            filePojo.setDescription(jsonObject.getString("description"));
                            filePojo.setCreatedBy(jsonObject.getString("created_by"));
                        }
                    }
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }

        return  filePojo;
    }
}


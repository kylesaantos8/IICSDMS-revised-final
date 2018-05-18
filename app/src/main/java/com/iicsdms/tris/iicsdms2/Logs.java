package com.iicsdms.tris.iicsdms2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Tris on 26/03/2018.
 */

public class Logs {
    private static final String TAG = "Testing";
    String line, result;
    InputStream inputStream;

    void Log(String linkLog, String type, String info, String user, String userType, String dept)
    {

        try
        {
            URL url = new URL(linkLog);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(type.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("info", "UTF-8")+"="+URLEncoder.encode(info.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("user", "UTF-8")+"="+URLEncoder.encode(user.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("userType", "UTF-8")+"="+URLEncoder.encode(userType.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("dept", "UTF-8")+"="+URLEncoder.encode(dept.trim(),"UTF-8");

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

    NotifPojo Logs(String linkLogs, String logEmail)
    {
        NotifPojo inboxPojo = new NotifPojo();
        try
        {
            URL url = new URL(linkLogs);
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
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
//                Toast.makeText(getContext(), "Unavailable. ", Toast.LENGTH_SHORT).show();
            }
            else
            {

                JSONArray jsonArray = new JSONArray(result.trim());
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    jsonObject = jsonArray.getJSONObject(i);

                    inboxPojo.setFullName(jsonObject.getString("full_name"));
                    inboxPojo.setUserType(jsonObject.getString("user_type"));
                    inboxPojo.setDept(jsonObject.getString("department"));
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }

        return inboxPojo;
    }

    InvitePojo LogsInvite(String linkLogs, String logEmail)
    {
        InvitePojo inboxPojo = new InvitePojo();
        try
        {
            URL url = new URL(linkLogs);
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
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
//                Toast.makeText(getContext(), "Unavailable. ", Toast.LENGTH_SHORT).show();
            }
            else
            {

                JSONArray jsonArray = new JSONArray(result.trim());
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    jsonObject = jsonArray.getJSONObject(i);

                    inboxPojo.setFullName(jsonObject.getString("full_name"));
                    inboxPojo.setType(jsonObject.getString("user_type"));
                    inboxPojo.setDept(jsonObject.getString("department"));
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }

        return inboxPojo;
    }
}